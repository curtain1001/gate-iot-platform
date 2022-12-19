package net.pingfang.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.pingfang.business.device.LaneDeviceSupport;
import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.gate.protocol.request.DeviceLifeCycleRequestPacket;
import net.pingfang.gate.protocol.values.DeviceInfo;
import net.pingfang.gate.protocol.values.LifeCycle;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-14 16:10
 */
@Component
public class ClientDeviceManager {
	@Resource
	private IBtpLaneService laneService;

	private String getLaneNo(Long laneId) {
		BtpLane lane = laneService.getById(laneId);
		return lane.getLaneNo();
	}

	// 添加设备
	public void add(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.create)//
				.deviceInfo(DeviceInfo.builder()//
						.deviceNo(device.getDeviceId())//
						.enabled(device.isEnabled())//
						.deviceProduct(device.getProduct())//
						.deviceName(device.getDeviceName())//
						.configuration(device.getConfiguration())//
						.build())
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}

	// 启动设备
	public void start(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.open)//
				.deviceInfo(DeviceInfo.builder()//
						.deviceNo(device.getDeviceId())//
						.enabled(device.isEnabled())//
						.deviceProduct(device.getProduct())//
						.deviceName(device.getDeviceName())//
						.configuration(device.getConfiguration())//
						.build())
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}

	// 关闭设备
	public void close(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.close)//
				.deviceInfo(DeviceInfo.builder()//
						.deviceNo(device.getDeviceId())//
						.enabled(device.isEnabled())//
						.deviceProduct(device.getProduct())//
						.deviceName(device.getDeviceName())//
						.configuration(device.getConfiguration())//
						.build())
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}

	// 修改设备信息
	public void update(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.close)//
				.deviceInfo(DeviceInfo.builder()//
						.deviceNo(device.getDeviceId())//
						.enabled(device.isEnabled())//
						.deviceProduct(device.getProduct())//
						.deviceName(device.getDeviceName())//
						.configuration(device.getConfiguration())//
						.build())
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}

	// 删除设备
	public void remove(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.remove)//
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}

	// 重启设备
	public void reboot(BtpDevice device) {
		DeviceLifeCycleRequestPacket requestPacket = DeviceLifeCycleRequestPacket.builder()
				.deviceNo(device.getDeviceId())//
				.lifeCycle(LifeCycle.reboot)//
				.build();
		LaneDeviceSupport.sendMsg(getLaneNo(device.getLaneId()), requestPacket);
	}
}
