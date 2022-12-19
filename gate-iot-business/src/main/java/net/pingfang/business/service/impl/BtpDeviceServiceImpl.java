package net.pingfang.business.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.events.DeviceClosedEvent;
import net.pingfang.business.events.DeviceDeletedEvent;
import net.pingfang.business.events.DeviceStartedEvent;
import net.pingfang.business.events.DeviceUpdatedEvent;
import net.pingfang.business.mapper.BtpDeviceMapper;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.business.values.DeviceStatus;
import net.pingfang.common.event.EventBusCenter;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:02
 */
@Service
public class BtpDeviceServiceImpl extends ServiceImpl<BtpDeviceMapper, BtpDevice> implements IBtpDeviceService {

	@Resource
	public BtpDeviceMapper deviceMapper;

	@Resource
	private IBtpLaneService laneService;

	@Resource
	private EventBusCenter eventBusCenter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(BtpDevice device) {
		int count = deviceMapper.insert(device);
		if (count > 0) {
			eventBusCenter.postAsync(DeviceStartedEvent.builder()//
					.deviceNo(device.getDeviceId())//
					.deviceName(device.getDeviceName())//
					.laneId(device.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateById(BtpDevice device) {
		int count = deviceMapper.updateById(device);
		if (count > 0) {
			eventBusCenter.postAsync(DeviceUpdatedEvent.builder()//
					.deviceNo(device.getDeviceId())//
					.deviceName(device.getDeviceName())//
					.laneId(device.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeById(Long id) {
		BtpDevice device = deviceMapper.selectById(id);
		if (device == null) {
			throw new RuntimeException("设备不存在");
		}
		if (device.getStatus() != DeviceStatus.OFFLINE) {
			throw new RuntimeException("删除设备前，请先关闭!");
		}
		int count = deviceMapper.deleteById(id);
		if (count > 0) {
			eventBusCenter.postAsync(DeviceDeletedEvent.builder()//
					.deviceNo(device.getDeviceId())//
					.laneId(device.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean open(Long id) {
		BtpDevice device = deviceMapper.selectById(id);
		if (device == null) {
			throw new RuntimeException("设备不存在");
		}
		if (device.getStatus() == DeviceStatus.ONLINE && device.isEnabled()) {
			throw new RuntimeException("设备在线状态，请勿重复开启操作!");
		}
		if (device.getStatus() == DeviceStatus.START && device.isEnabled()) {
			throw new RuntimeException("设备正在启动，请等待开启过程!");
		}
		device = device.toBuilder() //
				.status(DeviceStatus.START) //
				.enabled(true)//
				.build();
		int count = deviceMapper.updateById(device);
		if (count > 0) {
			eventBusCenter.postAsync(DeviceStartedEvent.builder()//
					.deviceNo(device.getDeviceId())//
					.laneId(device.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean close(Long id) {
		BtpDevice device = deviceMapper.selectById(id);
		if (device == null) {
			throw new RuntimeException("设备不存在");
		}
		if (!device.isEnabled()) {
			throw new RuntimeException("设备关闭状态，请勿重复关闭操作!");
		}
		device = device.toBuilder() //
				.status(DeviceStatus.CLOSE) //
				.enabled(false)//
				.build();
		int count = deviceMapper.updateById(device);
		if (count > 0) {
			eventBusCenter.postAsync(DeviceClosedEvent.builder()//
					.deviceNo(device.getDeviceId())//
					.laneId(device.getLaneId())//
					.build());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<BtpDevice> selectByLaneNo(String laneNo) {
		BtpLane btpLane = laneService.load(laneNo);
		if (btpLane == null) {
			throw new RuntimeException("该车道不存在");
		}
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, btpLane.getLaneId());
		return deviceMapper.selectList(queryWrapper);
	}

	@Override
	public List<BtpDevice> selectByLaneId(Long laneId) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, laneId);
		return deviceMapper.selectList(queryWrapper);
	}
}
