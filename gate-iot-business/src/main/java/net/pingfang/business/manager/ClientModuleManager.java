package net.pingfang.business.manager;

import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.pingfang.business.device.LaneDeviceSupport;
import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.domain.BtpModule;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.gate.protocol.request.ModuleLifeCycleRequestPacket;
import net.pingfang.gate.protocol.values.DeviceInfo;
import net.pingfang.gate.protocol.values.LifeCycle;
import net.pingfang.gate.protocol.values.ModuleInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-23 10:59
 */
public class ClientModuleManager {
	@Resource
	private IBtpLaneService laneService;

	private String getLaneNo(Long laneId) {
		BtpLane lane = laneService.getById(laneId);
		return lane.getLaneNo();
	}

	public void toClient(BtpModule module, LifeCycle lifeCycle) {
		ModuleLifeCycleRequestPacket requestPacket = generate(module, lifeCycle);
		LaneDeviceSupport.sendMsg(getLaneNo(module.getLaneId()), requestPacket);
	}

	private ModuleLifeCycleRequestPacket generate(BtpModule module, LifeCycle lifeCycle) {
		String laneNo = getLaneNo(module.getLaneId());
		return ModuleLifeCycleRequestPacket.builder().moduleCode(module.getModuleCode())//
				.lifeCycle(lifeCycle)//
				.laneNo(laneNo)//
				.moduleInfo(ModuleInfo.builder()//
						.id(module.getId())//
						.laneId(module.getLaneId()) //
						.moduleCode(module.getModuleCode())//
						.enabled(module.isEnabled())//
						.devices(module.getDeviceList().stream().map(x -> {
							return DeviceInfo.builder() //
									.deviceNo(x.getDeviceId())//
									.deviceName(x.getDeviceName())//
									.deviceProduct(x.getProduct())//
									.configuration(x.getConfiguration())//
									.enabled(x.isEnabled())//
									.build();
						}).collect(Collectors.toList())) //
						.configuration(module.getConfiguration())//
						.build())
				.build();
	}

}
