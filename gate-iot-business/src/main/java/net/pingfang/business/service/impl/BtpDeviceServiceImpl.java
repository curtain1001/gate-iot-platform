package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.mapper.BtpDeviceMapper;
import net.pingfang.business.service.IBtpDeviceService;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:02
 */
@Service
public class BtpDeviceServiceImpl extends ServiceImpl<BtpDeviceMapper, BtpDevice> implements IBtpDeviceService {
//
//	@Resource
//	public IBtpNetworkConfigService networkConfigService;
//
//	@Resource
//	public BtpDeviceMapper deviceMapper;
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean save(BtpDevice device) {
//		if (insertNetwork(device)) {
//			return deviceMapper.insert(device) > 0;
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean updateById(BtpDevice device) {
//		LambdaUpdateWrapper<BtpNetworkConfig> updateWrapper = Wrappers.lambdaUpdate();
//		updateWrapper.eq(BtpNetworkConfig::getNetworkConfigId, device.getDeviceId());
//		networkConfigService.remove(updateWrapper);
//		if (insertNetwork(device)) {
//			return deviceMapper.updateById(device) > 0;
//		}
//		return false;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean remove(Long deviceId) {
//		LambdaUpdateWrapper<BtpNetworkConfig> updateWrapper = Wrappers.lambdaUpdate();
//		updateWrapper.eq(BtpNetworkConfig::getNetworkConfigId, deviceId);
//		networkConfigService.remove(updateWrapper);
//		return deviceMapper.deleteById(deviceId) > 0;
//	}
//
//	@Override
//	@Transactional(rollbackFor = Exception.class)
//	public boolean removeByIds(Long[] deviceIds) {
//		List<Long> ids = Arrays.asList(deviceIds);
//		LambdaUpdateWrapper<BtpNetworkConfig> updateWrapper = Wrappers.lambdaUpdate();
//		updateWrapper.in(BtpNetworkConfig::getNetworkConfigId, ids);
//		networkConfigService.remove(updateWrapper);
//		return deviceMapper.deleteBatchIds(ids) > 0;
//	}
//
//	private boolean insertNetwork(BtpDevice device) {
//		Product product = ProductSupports.getSupport(device.getProduct());
//		if (device.getConfiguration() != null) {
//			device.getConfiguration().putAll(product.getDefaultProperties());
//		} else {
//			device.setConfiguration(product.getDefaultProperties());
//		}
//		BtpNetworkConfig config = BtpNetworkConfig.builder() //
//				.networkConfigId(device.getDeviceId())//
//				.name(networkConfigService.getNetworkNameByDevice(device.getDeviceName(), product.getNetwork())) //
//				.status(device.getStatus() == 0 ? NetworkConfigState.enabled : NetworkConfigState.disabled) //
//				.enabled(device.getEnabled() == 0)//
//				.configuration(device.getConfiguration())//
//				.type(product.getNetwork().getId())//
//				.control(Control.own) //
//				.createBy("device::" + device.getDeviceName())//
//				.createTime(new Date())//
//				.build();
//		return networkConfigService.save(config);
//	}
}
