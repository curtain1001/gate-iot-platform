package net.pingfang.flow.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.common.manager.AsyncManager;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.domain.FlowExecuteHistory;
import net.pingfang.flow.domain.FlowProcessInstance;
import net.pingfang.flow.enums.InstanceStatus;
import net.pingfang.flow.enums.ProcessStatus;
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowExecuteHistoryService;
import net.pingfang.flow.service.IFlowProcessInstanceService;
import net.pingfang.iot.common.instruction.InstructionManager;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 16:39
 */
@AllArgsConstructor
@Slf4j
@Component
//@EventBusListener(type = Type.ASYNC)
public class FlowEngine {

	@Resource
	private final IFlowDeploymentService deploymentService;
	@Resource
	private final IFlowProcessInstanceService processInstanceService;
	@Resource
	private final DeviceManager deviceManager;
	@Resource
	private final IFlowExecuteHistoryService historyService;
	@Resource
	private final InstructionManager instructionManager;
	@Resource
	private final EventBusCenter eventBusCenter;

	final Map<Long, ProcessTask> processStore = new ConcurrentHashMap<>();

	public void execute(Long laneId) {
		AsyncManager.me().execute(() -> {
			try {
				ProcessTask task = new ProcessTask(deploymentService, processInstanceService, deviceManager,
						historyService, instructionManager, eventBusCenter, laneId);
				processStore.put(laneId, task);
			} catch (Exception e) {
				log.error("流程异常；", e);
			}
		});
	}

	@PostConstruct
	public void sub() {
		instructionManager.subscribe(null, null, null)
				.filterWhen(x -> x.getLaneId() != null ? Mono.just(true) : Mono.just(false)).subscribe(x -> {
					LambdaQueryWrapper<FlowProcessInstance> processQueryWrapper = Wrappers.lambdaQuery();
					processQueryWrapper.eq(FlowProcessInstance::getLaneId, x.getLaneId());
					processQueryWrapper.eq(FlowProcessInstance::getStatus, InstanceStatus.IN_PROGRESS.name());
					FlowProcessInstance initInstance = processInstanceService.getOne(processQueryWrapper);
					if (initInstance != null) {
						LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
						queryWrapper.eq(FlowDeployment::getLaneId, x.getLaneId());
						queryWrapper.eq(FlowDeployment::getDeployId, initInstance.getDeployId());
						FlowDeployment deployment = this.deploymentService.getOne(queryWrapper);
						LambdaQueryWrapper<FlowExecuteHistory> historyLambdaQueryWrapper = Wrappers.lambdaQuery();
						historyLambdaQueryWrapper.eq(FlowExecuteHistory::getInstanceId, initInstance.getInstanceId());
						historyLambdaQueryWrapper.eq(FlowExecuteHistory::getStatus, ProcessStatus.WAIT.name());
						List<FlowExecuteHistory> executeHistories = historyService.list(historyLambdaQueryWrapper);
						if (executeHistories.isEmpty()) {
							new ProcessTask(deploymentService, processInstanceService, deviceManager, historyService,
									instructionManager, eventBusCenter, x.getLaneId());
						}
					}
				});
	}

//	@Subscribe
//	public void on(MessageUpEvent event) {
//		if (event.getInstruction() != null) {
//			ProcessMessage message = ProcessMessage.builder() //
//					.message(event.getMessage())//
//					.laneId(event.getLaneId())//
//					.instruction(event.getInstruction())//
//					.deviceId(event.getDeviceId())//
//					.product(event.getProduct())//
//					.type(event.getType())//
//					.build();
//			ProcessTask task = processStore.get(event.getLaneId());
//			if (task != null) {
//				task.put(message);
//			}
//		}
//	}

//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		List<FlowDeployment> deploymentList = deploymentService.list();
//		deploymentList.forEach(x -> {
//			execute(x.getLaneId());
//		});
//	}

}
