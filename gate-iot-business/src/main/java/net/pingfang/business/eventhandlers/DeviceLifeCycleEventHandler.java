package net.pingfang.business.eventhandlers;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.Subscribe;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.events.DeviceClosedEvent;
import net.pingfang.business.events.DeviceCreatedEvent;
import net.pingfang.business.events.DeviceDeletedEvent;
import net.pingfang.business.events.DeviceStartedEvent;
import net.pingfang.business.events.DeviceUpdatedEvent;
import net.pingfang.business.manager.ClientDeviceManager;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.common.event.EventBusListener;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-23 17:44
 */
@EventBusListener
public class DeviceLifeCycleEventHandler {
	@Resource
	private ClientDeviceManager clientDeviceManager;
	@Resource
	private IBtpDeviceService deviceService;

	@Subscribe
	public void on(DeviceCreatedEvent event) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, event.getLaneId());
		queryWrapper.eq(BtpDevice::getDeviceId, event.getDeviceNo());
		BtpDevice device = deviceService.getOne(queryWrapper);
		clientDeviceManager.add(device);
	}

	@Subscribe
	public void on(DeviceUpdatedEvent event) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, event.getLaneId());
		queryWrapper.eq(BtpDevice::getDeviceId, event.getDeviceNo());
		BtpDevice device = deviceService.getOne(queryWrapper);
		clientDeviceManager.update(device);
	}

	@Subscribe
	public void on(DeviceDeletedEvent event) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, event.getLaneId());
		queryWrapper.eq(BtpDevice::getDeviceId, event.getDeviceNo());
		BtpDevice device = deviceService.getOne(queryWrapper);
		clientDeviceManager.remove(device);
	}

	@Subscribe
	public void on(DeviceStartedEvent event) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, event.getLaneId());
		queryWrapper.eq(BtpDevice::getDeviceId, event.getDeviceNo());
		BtpDevice device = deviceService.getOne(queryWrapper);
		clientDeviceManager.start(device);
	}

	@Subscribe
	public void on(DeviceClosedEvent event) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, event.getLaneId());
		queryWrapper.eq(BtpDevice::getDeviceId, event.getDeviceNo());
		BtpDevice device = deviceService.getOne(queryWrapper);
		clientDeviceManager.close(device);
	}

}
