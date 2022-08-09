package net.pingfang.flow.core;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.domain.FlowEdge;
import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.domain.FlowProcessInstance;
import net.pingfang.flow.enums.InstanceStatus;
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowProcessInstanceService;
import net.pingfang.flow.utils.FlowUtils;
import net.pingfang.flow.values.ProcessMessage;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.ObjectType;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;

/**
 * <p>
 * 流程执行实例
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 15:48
 */
@Slf4j
public class ProcessTask {
	private IFlowDeploymentService deploymentService;
	private IFlowProcessInstanceService processInstanceService;

	private final EmitterProcessor<ProcessMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<ProcessMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	/**
	 * 流程id
	 */
	private Long instanceId;

	private final Long laneId;

	private List<FlowNode> flowNodes = Lists.newArrayList();

	private List<FlowEdge> flowEdges = Lists.newArrayList();

	private FlowNode currentNode;

	private List<FlowNode> startNode;

	public ProcessTask(Long laneId) {
		this.laneId = laneId;
		processor.subscribe(x -> {
			if (instanceId == null) {
				LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
				queryWrapper.eq(FlowDeployment::getLaneId, this.laneId);
				FlowDeployment deployment = deploymentService.getOne(queryWrapper);
				List<FlowNode> flowNodes = getStartNode(deployment);
				if (CollectionUtils.isEmpty(flowNodes)) {
					log.error("流程有误：不存在开始节点");
					return;
				}
				List<FlowNode> checked = flowNodes.stream().filter(node -> checkNode(x, node))
						.collect(Collectors.toList());
				if (CollectionUtils.isEmpty(checked)) {
					return;
				}
				// 开始流程命中 流程开始
				FlowProcessInstance instance = FlowProcessInstance.builder() //
						.laneId(laneId)//
						.deployFlowId(deployment.getFlowId())//
						.startTime(new Date())//
						.status(InstanceStatus.IN_PROGRESS)//
						.build();
				processInstanceService.save(instance);//
				// 执行指令
			}
		});
	}

	public void run(Instruction instruction) {
//		if ()
	}

	public void checkProcessInstance() {
		FlowProcessInstance processInstance = processInstanceService.getById(laneId);
		if (processInstance != null) {
			this.instanceId = processInstance.getInstanceId();
		}
	}

	public boolean checkNode(ProcessMessage processMessage, FlowNode flowNode) {
		if (processMessage.getProduct().getType() == flowNode.getProperties().getObjectType()) {
			if (processMessage.getProduct().getType() == ObjectType.device) {
				return processMessage.getDeviceId().equals(flowNode.getProperties().getDeviceId())
						&& processMessage.getInstruction() == flowNode.getProperties().getInstruction();
			} else if (processMessage.getProduct().getType() == ObjectType.service) {
				return processMessage.getInstruction() == flowNode.getProperties().getInstruction();
			}
		}
		return false;
	}

	public List<FlowNode> getStartNode(FlowDeployment deployment) {
		if (deployment != null && deployment.getContent() != null) {
			JsonNode jsonNode = deployment.getContent();
			// 节点
			JsonNode nodes = jsonNode.get("nodes");
			if (nodes != null && nodes.isArray()) {
				List<FlowNode> startNodes = Lists.newArrayList();
				nodes.elements().forEachRemaining(x -> {
					if (x.hasNonNull("type") && "start".equals(x.get("type").asText())) {
						FlowNode flowNode = FlowUtils.nodeConvert(x);
						if (flowNode != null) {
							flowNode = flowNode.toBuilder() //
									.deployId(deployment.getDeployId())//
									.build();
							flowNodes.add(flowNode);
						}
					}
				});
			}
		}
	}

	private void setFlow(FlowDeployment deployment) {
		if (deployment != null && deployment.getContent() != null) {
			JsonNode jsonNode = deployment.getContent();
			// 节点
			JsonNode nodes = jsonNode.get("nodes");
			if (nodes != null && nodes.isArray()) {
				AtomicInteger i = new AtomicInteger(0);
				nodes.elements().forEachRemaining(x -> {
					i.getAndIncrement();
					FlowNode flowNode = FlowUtils.nodeConvert(x);
					if (flowNode != null) {
						flowNode = flowNode.toBuilder() //
								.deployId(deployment.getDeployId())//
								.seq(i.get()) //
								.build();
						flowNodes.add(flowNode);
					}
				});
			}
			// 连线
			JsonNode edges = jsonNode.get("edges");
			if (edges != null && edges.isArray()) {
				AtomicInteger i = new AtomicInteger(0);
				edges.elements().forEachRemaining(x -> {
					i.getAndIncrement();
					FlowEdge flowEdge = FlowUtils.edgeConvert(x);
					if (flowEdge != null) {
						flowEdge = flowEdge.toBuilder() //
								.deployId(deployment.getDeployId())//
								.seq(i.get()) //
								.build();
						flowEdges.add(flowEdge);
					}
				});
			}
		}

	}

	/**
	 * 接受订阅消息
	 *
	 * @param processMessage 流程信息
	 */
	public void received(ProcessMessage processMessage) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn(" message pending {} ,drop message:{}", processor.getPending(), processMessage.toString());
			return;
		}
		sink.next(processMessage);
	}
}
