package net.pingfang.business.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import net.pingfang.business.domain.BtpServiceDevice;
import net.pingfang.business.domain.BtpSupportService;
import net.pingfang.business.mapper.BtpSupportServiceMapper;
import net.pingfang.business.service.IBtpServiceDeviceService;
import net.pingfang.business.service.IBtpSupportServiceService;
import net.pingfang.services.ServiceProduct;
import net.pingfang.services.ServiceProductSupports;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 10:55
 */
@Service
public class BtpSupportServiceServiceImpl extends ServiceImpl<BtpSupportServiceMapper, BtpSupportService>
		implements IBtpSupportServiceService {
	@Resource
	BtpSupportServiceMapper supportServiceMapper;
	@Resource
	IBtpServiceDeviceService serviceDeviceService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(BtpSupportService supportService) {
		boolean bln = super.save(supportService);
		if (bln) {
			List<Long> deviceIds = supportService.getDeviceIds();
			if (CollectionUtils.isNotEmpty(deviceIds)) {
				List<BtpServiceDevice> serviceDevices = deviceIds.stream().map(x -> {
					return BtpServiceDevice.builder().serviceId(supportService.getId())//
							.deviceId(x)//
							.build();
				}).collect(Collectors.toList());
				serviceDeviceService.saveBatch(serviceDevices);
			}
		} else {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(BtpSupportService entity) {
		boolean bln = super.updateById(entity);
		if (bln) {
			LambdaUpdateWrapper<BtpServiceDevice> updateWrapper = Wrappers.lambdaUpdate();
			updateWrapper.eq(BtpServiceDevice::getServiceId, entity.getId());
			serviceDeviceService.remove(updateWrapper);
			List<Long> deviceIds = entity.getDeviceIds();
			if (CollectionUtils.isNotEmpty(deviceIds)) {
				List<BtpServiceDevice> serviceDevices = deviceIds.stream().map(x -> {
					return BtpServiceDevice.builder().serviceId(entity.getId())//
							.deviceId(x)//
							.build();
				}).collect(Collectors.toList());
				serviceDeviceService.saveBatch(serviceDevices);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeById(Serializable id) {
		boolean bln = super.removeById(id);
		if (bln) {
			LambdaUpdateWrapper<BtpServiceDevice> updateWrapper = Wrappers.lambdaUpdate();
			updateWrapper.eq(BtpServiceDevice::getServiceId, id);
			serviceDeviceService.remove(updateWrapper);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addDevice(long laneId, long deviceId, String serviceCode) {
		Optional<ServiceProduct> optional = ServiceProductSupports.lookup(serviceCode);
		if (!optional.isPresent()) {
			throw new RuntimeException("服务类型不存在！");
		}
		LambdaQueryWrapper<BtpSupportService> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpSupportService::getLaneId, laneId);
		wrapper.eq(BtpSupportService::getServiceCode, serviceCode);
		BtpSupportService btpSupportService = supportServiceMapper.selectOne(wrapper);
		if (btpSupportService != null) {
			List<Long> deviceIds = btpSupportService.getDeviceIds();
			if (CollectionUtils.isNotEmpty(deviceIds)) {
				deviceIds.add(deviceId);

			} else {
				deviceIds = Lists.newArrayList(deviceId);
			}
			btpSupportService = btpSupportService.toBuilder() //
					.deviceIds(deviceIds) //
					.build();
			supportServiceMapper.updateById(btpSupportService);
		} else {
			BtpSupportService supportService = BtpSupportService.builder() //
					.deviceIds(Lists.newArrayList(deviceId))//
					.laneId(laneId)//
					.serviceCode(serviceCode)//
					.serviceName(optional.get().getName()) //
					.build();
			supportServiceMapper.insert(supportService);
		}
		return true;
	}
}
