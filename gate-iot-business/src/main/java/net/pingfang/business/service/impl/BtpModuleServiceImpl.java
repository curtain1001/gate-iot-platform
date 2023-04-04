package net.pingfang.business.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpModule;
import net.pingfang.business.events.ModuleStartedEvent;
import net.pingfang.business.events.ModuleUpdatedEvent;
import net.pingfang.business.mapper.BtpDeviceMapper;
import net.pingfang.business.mapper.BtpModuleMapper;
import net.pingfang.business.service.IBtpModuleService;
import net.pingfang.common.event.EventBusCenter;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-05 10:21
 */
@Service
public class BtpModuleServiceImpl extends ServiceImpl<BtpModuleMapper, BtpModule> implements IBtpModuleService {
	@Resource
	BtpModuleMapper btpModuleMapper;

	@Resource
	BtpDeviceMapper btpDeviceMapper;

	@Resource
	private EventBusCenter eventBusCenter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(BtpModule module) {
		int count = btpModuleMapper.insert(module);
		if (count > 0) {
			eventBusCenter.postAsync(ModuleStartedEvent.builder()//
					.moduleCode(module.getDeviceId())//
					.laneId(module.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateById(BtpModule module) {
		int count = btpModuleMapper.updateById(module);
		if (count > 0) {
			eventBusCenter.postAsync(ModuleUpdatedEvent.builder()//
					.moduleCode(module.getModuleCode())//
					.laneId(module.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeById(Long id) {
		BtpModule module = btpModuleMapper.selectById(id);
		if (module == null) {
			throw new RuntimeException("设备不存在");
		}
//		if (device.getStatus() != DeviceStatus.OFFLINE) {
//			throw new RuntimeException("删除设备前，请先关闭!");
//		}
//		int count = deviceMapper.deleteById(id);
//		if (count > 0) {
//			eventBusCenter.postAsync(ModuleDeletedEvent.builder()//
//					.deviceNo(device.getDeviceId())//
//					.laneId(device.getLaneId())//
//					.build());
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	@Override
	public boolean open(Long id) {
//		BtpModule device = deviceMapper.selectById(id);
//		if (device == null) {
//			throw new RuntimeException("设备不存在");
//		}
//		if (device.getStatus() == DeviceStatus.ONLINE && device.isEnabled()) {
//			throw new RuntimeException("设备在线状态，请勿重复开启操作!");
//		}
//		if (device.getStatus() == DeviceStatus.START && device.isEnabled()) {
//			throw new RuntimeException("设备正在启动，请等待开启过程!");
//		}
//		device = device.toBuilder() //
//				.status(DeviceStatus.START) //
//				.enabled(true)//
//				.build();
//		int count = deviceMapper.updateById(device);
//		if (count > 0) {
//			eventBusCenter.postAsync(ModuleStartedEvent.builder()//
//					.deviceNo(device.getDeviceId())//
//					.laneId(device.getLaneId())//
//					.build());
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	@Override
	public boolean close(Long id) {
//		BtpDevice device = deviceMapper.selectById(id);
//		if (device == null) {
//			throw new RuntimeException("设备不存在");
//		}
//		if (!device.isEnabled()) {
//			throw new RuntimeException("设备关闭状态，请勿重复关闭操作!");
//		}
//		device = device.toBuilder() //
//				.status(DeviceStatus.CLOSE) //
//				.enabled(false)//
//				.build();
//		int count = deviceMapper.updateById(device);
//		if (count > 0) {
//			eventBusCenter.postAsync(ModuleClosedEvent.builder()//
//					.deviceNo(device.getDeviceId())//
//					.laneId(device.getLaneId())//
//					.build());
//			return true;
//		} else {
//			return false;
//		}
		return false;
	}

	@Override
	public List<BtpModule> selectByLaneId(long laneId) {
		LambdaQueryWrapper<BtpModule> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpModule::getLaneId, laneId);
		List<BtpModule> modules = btpModuleMapper.selectList(queryWrapper);
		modules = modules.stream().map(x -> {
			if (CollectionUtils.isNotEmpty(x.getDeviceIds())) {
				LambdaQueryWrapper<BtpDevice> deviceLambdaQueryWrapper = Wrappers.lambdaQuery();
				deviceLambdaQueryWrapper.in(BtpDevice::getDeviceId, x.getDeviceIds());
				List<BtpDevice> deviceList = btpDeviceMapper.selectList(deviceLambdaQueryWrapper);
				if (CollectionUtils.isNotEmpty(deviceList)) {
					return x.toBuilder() //
							.deviceList(deviceList) //
							.build(); //
				} else {
					return x;
				}
			} else {
				return x;
			}
		}).collect(Collectors.toList());
		return modules;
	}

	@Override
	public BtpModule selectByCode(long laneId, String moduleCode) {
		LambdaQueryWrapper<BtpModule> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpModule::getLaneId, laneId);
		queryWrapper.eq(BtpModule::getModuleCode, moduleCode);
		BtpModule modules = btpModuleMapper.selectOne(queryWrapper);
		if (CollectionUtils.isNotEmpty(modules.getDeviceIds())) {
			LambdaQueryWrapper<BtpDevice> deviceLambdaQueryWrapper = Wrappers.lambdaQuery();
			deviceLambdaQueryWrapper.in(BtpDevice::getDeviceId, modules.getDeviceIds());
			List<BtpDevice> deviceList = btpDeviceMapper.selectList(deviceLambdaQueryWrapper);
			if (CollectionUtils.isNotEmpty(deviceList)) {
				return modules.toBuilder() //
						.deviceList(deviceList) //
						.build(); //
			}
		}
		return modules;
	}
}
