package net.pingfang.flow.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.event.MessageUpEvent;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowExecuteHistoryService;
import net.pingfang.flow.service.IFlowProcessInstanceService;
import net.pingfang.flow.values.ProcessMessage;
import net.pingfang.iot.common.instruction.InstructionManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 16:39
 */
@AllArgsConstructor
@Slf4j
@Component
public class FlowEngine implements ApplicationRunner {

	@Resource
	private final IFlowDeploymentService deploymentService;
	@Resource
	private IFlowProcessInstanceService processInstanceService;
	@Resource
	private DeviceManager deviceManager;
	@Resource
	private IFlowExecuteHistoryService historyService;
	@Resource
	private InstructionManager instructionManager;

	final Map<Long, ProcessTask> processStore = new ConcurrentHashMap<>();

	public void execute(Long laneId) {
		ProcessTask task = new ProcessTask(deploymentService, processInstanceService, deviceManager, historyService,
				instructionManager, laneId);
		processStore.put(laneId, task);
	}

	@Subscribe
	public void on(MessageUpEvent event) {
		log.info("设备：{}；指令：{}；", event.getDeviceId(), event.getInstruction().getValue());
		ProcessMessage message = ProcessMessage.builder() //
				.message(event.getMessage())//
				.laneId(event.getLaneId())//
				.instruction(event.getInstruction())//
				.deviceId(event.getDeviceId())//
				.product(event.getProduct())//
				.type(event.getType())//
				.build();
		processStore.get(event.getLaneId()).put(message);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<FlowDeployment> deploymentList = deploymentService.list();
		deploymentList.forEach(x -> {
			execute(x.getLaneId());
		});
	}

}
