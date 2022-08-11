package net.pingfang.flow.core;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.dockservice.core.BusinessInstruction;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.domain.FlowEdge;
import net.pingfang.flow.domain.FlowExecuteHistory;
import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.domain.FlowProcessInstance;
import net.pingfang.flow.enums.InstanceStatus;
import net.pingfang.flow.enums.NodeType;
import net.pingfang.flow.enums.ProcessStatus;
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowExecuteHistoryService;
import net.pingfang.flow.service.IFlowProcessInstanceService;
import net.pingfang.flow.utils.FlowUtils;
import net.pingfang.flow.values.EdgeProperties;
import net.pingfang.flow.values.NodeProperties;
import net.pingfang.flow.values.ProcessMessage;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
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
	private final IFlowDeploymentService deploymentService;
	private final IFlowProcessInstanceService processInstanceService;
	private final DeviceManager deviceManager;
	private final IFlowExecuteHistoryService historyService;
	public final InstructionManager instructionManager;

	private final EmitterProcessor<ProcessMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<ProcessMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	private final List<NodeType> types = Lists.newArrayList(NodeType.start, NodeType.server, NodeType.device);

	private final List<NodeType> logicType = Lists.newArrayList(NodeType.or, NodeType.and);

	/**
	 * 流程id
	 */
	private Long instanceId;
	/**
	 * 车道id
	 */
	private final Long laneId;
	/**
	 * 流程节点集
	 */
	private final List<FlowNode> flowNodes = new CopyOnWriteArrayList<FlowNode>();
	/**
	 * 流程节点连线集
	 */
	private final List<FlowEdge> flowEdges = new CopyOnWriteArrayList<FlowEdge>();
	/**
	 * 当前节点
	 */
	private final List<FlowNode> currentNodes = new CopyOnWriteArrayList<FlowNode>();
	/**
	 * 开始节点
	 */
	private final List<FlowNode> startNode = new CopyOnWriteArrayList<FlowNode>();

	/**
	 * 流程处理结果
	 */
	private ObjectNode result;

	public void put(ProcessMessage message) {
		sink.next(message);
	}

	public ProcessTask(IFlowDeploymentService deploymentService, IFlowProcessInstanceService processInstanceService,
			DeviceManager deviceManager, IFlowExecuteHistoryService historyService,
			InstructionManager instructionManager, Long laneId) {
		this.deploymentService = deploymentService;
		this.processInstanceService = processInstanceService;
		this.deviceManager = deviceManager;
		this.historyService = historyService;
		this.instructionManager = instructionManager;
		this.laneId = laneId;
		LambdaQueryWrapper<FlowProcessInstance> processQueryWrapper = Wrappers.lambdaQuery();
		processQueryWrapper.eq(FlowProcessInstance::getLaneId, this.laneId);
		processQueryWrapper.eq(FlowProcessInstance::getStatus, InstanceStatus.IN_PROGRESS.name());
		FlowProcessInstance initInstance = processInstanceService.getOne(processQueryWrapper);
		if (initInstance != null) {
			this.instanceId = initInstance.getInstanceId();
			LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
			queryWrapper.eq(FlowDeployment::getLaneId, this.laneId);
			FlowDeployment deployment = this.deploymentService.getOne(queryWrapper);
			setFlow(deployment);
			LambdaQueryWrapper<FlowExecuteHistory> historyLambdaQueryWrapper = Wrappers.lambdaQuery();
			historyLambdaQueryWrapper.eq(FlowExecuteHistory::getInstanceId, this.instanceId);
			historyLambdaQueryWrapper.eq(FlowExecuteHistory::getStatus, ProcessStatus.WAIT.name());
			List<FlowExecuteHistory> executeHistories = historyService.list(historyLambdaQueryWrapper);
			List<String> currentNodeIds = executeHistories.stream().map(FlowExecuteHistory::getNodeId)
					.collect(Collectors.toList());
			this.currentNodes.addAll(flowNodes.stream().filter(x -> currentNodeIds.contains(x.getNodeId()))
					.collect(Collectors.toList()));
		}

		processor.subscribe(x -> {
			try {
				if (this.laneId != null) {
					checkProcessInstance();
				}
				if (this.instanceId == null) {
					LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
					queryWrapper.eq(FlowDeployment::getLaneId, this.laneId);
					FlowDeployment deployment = this.deploymentService.getOne(queryWrapper);
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
					this.startNode.addAll(checked);
					// 开始流程命中 流程开始
					FlowProcessInstance instance = FlowProcessInstance.builder() //
							.laneId(laneId)//
							.deployFlowId(deployment.getFlowId())//
							.startTime(new Date())//
							.status(InstanceStatus.IN_PROGRESS)//
							.build();
					processInstanceService.save(instance);//
					this.instanceId = instance.getInstanceId();
					// 设置完整流程节点信息
					setFlow(deployment);
					// 初始化返回报文内容
					initResult();
					// 当前节点设置
					checked.forEach(this::setCurrentNode);
					// 赋值
					if (x.getMessage() != null) {
						assemble(this.currentNodes.get(0).getNodeName(), JsonUtils.toJsonNode(x.getMessage()));
					}
					List<FlowNode> nodes = Lists.newArrayList(this.currentNodes);
					nodes.forEach(node -> finishNode(node, InstructionResult.success(x.getMessage(), "")));
					List<FlowNode> nextNodes = nodes.stream().map(this::getNextNode).flatMap(Collection::parallelStream)
							.collect(Collectors.toList());
					// 同层多节点时分支处理
					transfer(nextNodes);
				} else {
					// 流程中
					List<FlowNode> checked = this.currentNodes.stream().filter(node -> checkNode(x, node))
							.collect(Collectors.toList());
					if (CollectionUtils.isEmpty(checked)) {
						return;
					}
					// 节点命中
					transfer(checked);
				}
			} catch (Exception e) {
				log.error("异常:", e);
			}

		});
	}

	public void transfer(List<FlowNode> flowNodes) {
		if (!checkProcessInstance()) {
			return;
		}
		Iterator<FlowNode> iterator = flowNodes.listIterator();
		while (iterator.hasNext()) {
			FlowNode node = iterator.next();
			if (types.contains(node.getType())) {
				// 下行节点
				if (node.getProperties().getInsType().equals(InstructionType.down)) {
					InstructionResult instructionResult = exec(node);
					// 结束当前节点
					finishNode(node, instructionResult);
					this.currentNodes.remove(node);
					if (instructionResult.isSuccess()) {
						List<FlowNode> nextNodes = getNextNode(node);
						nextNodes.forEach(this::setCurrentNode);
						transfer(nextNodes);
					}
				}
			} else if (logicType.contains(node.getType())) {
				// 节点逻辑处理
				logicNode(node);
			}
		}
	}

	public void logicNode(FlowNode node) {
		List<String> sourceNodeIds;
		List<FlowNode> targetNodes;
		switch (node.getType()) {
		case or:
			sourceNodeIds = flowEdges.stream().filter(edge -> edge.getTargetNodeId().equals(node.getNodeId()))
					.map(FlowEdge::getSourceNodeId).collect(Collectors.toList());
			targetNodes = flowNodes.stream().filter(n -> sourceNodeIds.contains(n.getNodeId()))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(targetNodes)) {
				LambdaQueryWrapper<FlowExecuteHistory> queryWrapper = Wrappers.lambdaQuery();
				queryWrapper.in(FlowExecuteHistory::getNodeId,
						targetNodes.stream().map(FlowNode::getNodeId).collect(Collectors.toList()));
				List<FlowExecuteHistory> findNodes = historyService.list(queryWrapper);
				if (findNodes.stream().anyMatch(x -> x.getStatus() == ProcessStatus.SUCCESS)) {
					// 结束当前节点
					finishNode(node, InstructionResult.success(null, ""));
					this.currentNodes.remove(node);
					List<FlowNode> nextNodes = getNextNode(node);
					nextNodes.forEach(this::setCurrentNode);
					transfer(nextNodes);
				}
			}
			break;
		case and:
			sourceNodeIds = flowEdges.stream().filter(edge -> edge.getTargetNodeId().equals(node.getNodeId()))
					.map(FlowEdge::getSourceNodeId).collect(Collectors.toList());
			targetNodes = flowNodes.stream().filter(n -> sourceNodeIds.contains(n.getNodeId()))
					.collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(targetNodes)) {
				LambdaQueryWrapper<FlowExecuteHistory> queryWrapper = Wrappers.lambdaQuery();
				queryWrapper.in(FlowExecuteHistory::getNodeId,
						targetNodes.stream().map(FlowNode::getNodeId).collect(Collectors.toList()));
				List<FlowExecuteHistory> findNodes = historyService.list(queryWrapper);
				if (findNodes.stream().allMatch(x -> x.getStatus() == ProcessStatus.SUCCESS)) {
					// 结束当前节点
					finishNode(node, InstructionResult.success(null, ""));
					this.currentNodes.remove(node);
					List<FlowNode> nextNodes = getNextNode(node);
					nextNodes.forEach(this::setCurrentNode);
					transfer(nextNodes);
				}
			}
			break;
		case end:
			// 下行节点
			if (node.getProperties().getInsType().equals(InstructionType.down)) {
				InstructionResult instructionResult = exec(node);
				// 结束当前节点
				finishNode(node, instructionResult);
				this.currentNodes.remove(node);
			}
			flowEnd();

			break;
		}

	}

	/**
	 * 指令执行
	 *
	 * @param node
	 */
	public InstructionResult exec(FlowNode node) {
		InstructionResult instructionResult = null;
		if (node.getProperties().getInsType().equals(InstructionType.down)) {
			if (node.getProperties().getInstruction() instanceof DeviceInstruction) {
				DeviceInstruction deviceInstruction = (DeviceInstruction) node.getProperties().getInstruction();
				DeviceOperator deviceOperator = deviceManager.getDevice(laneId, node.getProperties().getDeviceId());
				instructionResult = deviceInstruction.execution(deviceOperator);
			} else if (node.getProperties().getInstruction() instanceof BusinessInstruction) {
				BusinessInstruction businessInstruction = (BusinessInstruction) node.getProperties().getInstruction();
				instructionResult = businessInstruction.execution(result);
			}
		}
		return instructionResult;
	}

	/**
	 * 获取下一节点
	 *
	 * @param node
	 * @return
	 */
	public List<FlowNode> getNextNode(FlowNode node) {
		List<String> targetNodeIds = this.flowEdges.stream().filter(x -> {
			if (x.getSourceNodeId().equals(node.getNodeId())) {
				EdgeProperties properties = x.getProperties();
				try {
					if (properties != null && StringUtils.isNotEmpty(properties.getCondition())) {
						Object o = FlowUtils.execJs(properties.getCondition(), result);
						return (Boolean) o;
					} else {
						return true;
					}
				} catch (Exception e) {
					log.error("JS Engine exec error:", e);
					return false;
				}
			}
			return false;
		}).map(FlowEdge::getTargetNodeId).collect(Collectors.toList());
		return flowNodes.stream().filter(x -> targetNodeIds.contains(x.getNodeId())).collect(Collectors.toList());
	}

	/**
	 * 节点开始执行
	 *
	 * @param currentNode 当前节点
	 */
	public void setCurrentNode(FlowNode currentNode) {
		FlowExecuteHistory executeHistory = FlowExecuteHistory.builder() //
				.instruction(currentNode.getProperties().getInstruction().getValue())//
				.instanceId(instanceId) //
				.nodeId(currentNode.getNodeId()) //
				.nodeName(currentNode.getNodeName())//
				.status(ProcessStatus.WAIT)//
				.createTime(new Date())//
				.build();
		historyService.save(executeHistory);
		this.currentNodes.add(currentNode);
	}

	/**
	 * 节点完成
	 *
	 * @param node              当前节点
	 * @param instructionResult 当前节点指令完成结果
	 */
	public void finishNode(FlowNode node, InstructionResult instructionResult) {
		LambdaQueryWrapper<FlowExecuteHistory> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(FlowExecuteHistory::getInstanceId, this.instanceId);
		queryWrapper.eq(FlowExecuteHistory::getNodeId, node.getNodeId());
		FlowExecuteHistory executeHistory = historyService.getOne(queryWrapper);
		executeHistory = executeHistory.toBuilder()//
				.status(instructionResult.isSuccess() ? ProcessStatus.SUCCESS : ProcessStatus.FAIL)//
				.updateTime(new Date())//
				.result(JsonUtils.toJsonString(instructionResult)) //
				.build();
		assemble(node.getNodeName(), JsonUtils.toJsonNode(instructionResult.getResult()));
		historyService.updateById(executeHistory);
		this.currentNodes.remove(node);
	}

	public void flowEnd() {
		FlowProcessInstance instance = processInstanceService.getById(this.instanceId);
		instance = instance.toBuilder() //
				.status(InstanceStatus.FINISHED) //
				.endTime(new Date()) //
				.build();
		processInstanceService.updateById(instance);
		this.instanceId = null;
		this.flowNodes.clear();
		this.currentNodes.clear();
		this.flowEdges.clear();
		this.startNode.clear();
	}

	/**
	 * 核验流程实例
	 */
	public boolean checkProcessInstance() {
		FlowProcessInstance processInstance = processInstanceService.getById(this.instanceId);
		if (processInstance == null) {
			// todo 结束流程
			this.instanceId = null;
			this.flowNodes.clear();
			this.currentNodes.clear();
			this.flowEdges.clear();
			this.startNode.clear();
			return false;
		} else if (!processInstance.getStatus().inProgress()) {
			this.instanceId = null;
			this.flowNodes.clear();
			this.currentNodes.clear();
			this.flowEdges.clear();
			this.startNode.clear();
		}
		return true;
	}

	/**
	 * 核验节点 判断节点与事件是否一致
	 *
	 * @param processMessage
	 * @param flowNode
	 * @return
	 */
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

	/**
	 * 获取开始节点
	 *
	 * @param deployment
	 * @return
	 */
	public List<FlowNode> getStartNode(FlowDeployment deployment) {
		List<FlowNode> startNodes = Lists.newArrayList();
		if (deployment != null && deployment.getContent() != null) {
			JsonNode jsonNode = deployment.getContent();
			// 节点
			JsonNode nodes = jsonNode.get("nodes");
			if (nodes != null && nodes.isArray()) {
				nodes.elements().forEachRemaining(x -> {
					if (x.hasNonNull("type") && "start".equals(x.get("type").asText())) {
						FlowNode flowNode = nodeConvert(x);
						if (flowNode != null) {
							flowNode = flowNode.toBuilder() //
									.deployId(deployment.getDeployId())//
									.build();
							startNodes.add(flowNode);
						}
					}
				});
			}
		}
		return startNodes;
	}

	/**
	 * 设置节点与连线信息
	 *
	 * @param deployment
	 */

	private void setFlow(FlowDeployment deployment) {
		if (deployment != null && deployment.getContent() != null) {
			JsonNode jsonNode = deployment.getContent();
			// 节点
			JsonNode nodes = jsonNode.get("nodes");
			if (nodes != null && nodes.isArray()) {
				AtomicInteger i = new AtomicInteger(0);
				nodes.elements().forEachRemaining(x -> {
					i.getAndIncrement();
					FlowNode flowNode = nodeConvert(x);
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
					FlowEdge flowEdge = edgeConvert(x);
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

	/**
	 * 初始化基础结果报文
	 */
	public void initResult() {
		if (result == null) {
			result = JsonUtils.MAPPER.createObjectNode();
		}
		result.put("laneId", this.laneId);
		result.put("instanceId", this.instanceId);
	}

	/**
	 * 拼装流程结果
	 *
	 * @param field
	 * @param node
	 */
	public void assemble(String field, Object node) {
		if (result == null) {
			result = JsonUtils.MAPPER.createObjectNode();
		}
		result.put(field, JsonUtils.toJsonNode(node));
	}

	public FlowNode nodeConvert(JsonNode jsonNode) {
		JsonNode properties = jsonNode.get("properties");

		NodeProperties nodeProperties = NodeProperties.builder() //
				.instruction(JsonUtils.getFieldValue(properties, "instruction") != null
						? instructionManager.getInstruction(JsonUtils.getFieldValue(properties, "instruction"))
						: null)//
				.deviceId(JsonUtils.getFieldValue(properties, "deviceId"))//
				.insType(JsonUtils.getFieldValue(properties, "insType") != null
						? InstructionType.valueOf(JsonUtils.getFieldValue(properties, "insType"))
						: null)
				.objectType(JsonUtils.getFieldValue(properties, "objectType") != null
						? ObjectType.valueOf(JsonUtils.getFieldValue(properties, "objectType"))
						: null)
				.build();

		return FlowNode.builder() //
				// .deployId()
				.nodeId(jsonNode.get("id").asText())//
				.type(JsonUtils.getFieldValue(jsonNode, "type") != null
						? NodeType.valueOf(JsonUtils.getFieldValue(jsonNode, "type"))
						: null)//
				.nodeName(JsonUtils.getFieldValue(jsonNode, "text") != null ? jsonNode.get("text").get("value").asText()
						: "")//
				.content(jsonNode)//
				.properties(nodeProperties)//
				.build();
	}

	public FlowEdge edgeConvert(JsonNode edge) {
		JsonNode properties = edge.get("properties");
		FlowEdge flowEdge = FlowEdge.builder() //
				// .deployId()
				.edgeId(edge.get("id").asText())//
				.type(edge.get("type").asText())//
				.sourceNodeId(edge.get("sourceNodeId").asText())//
				.targetNodeId(edge.get("targetNodeId").asText())//
				.content(edge)//
				.build();
		flowEdge = flowEdge.setProperties(properties);
		return flowEdge;
	}

}
