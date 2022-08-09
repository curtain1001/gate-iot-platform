package net.pingfang.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.business.domain.BtpFlow;
import net.pingfang.business.mapper.BtpFlowMapper;
import net.pingfang.business.service.IBtpFlowService;
import net.pingfang.common.exception.ServiceException;
import net.pingfang.common.utils.SecurityUtils;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.domain.FlowEdge;
import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowEdgeService;
import net.pingfang.flow.service.IFlowNodeService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 17:20
 */
@Service
public class BtpFlowServiceImpl extends ServiceImpl<BtpFlowMapper, BtpFlow> implements IBtpFlowService {

	@Resource
	public BtpFlowMapper flowMapper;
	@Resource
	public IFlowNodeService nodeService;
	@Resource
	public IFlowEdgeService edgeService;
	@Resource
	public IFlowDeploymentService deploymentService;

	@Transactional(rollbackFor = { Exception.class })
	public boolean deploy(Long laneId, Long flowId, int version) {

		LambdaQueryWrapper<FlowDeployment> deploymentLambdaQueryWrapper = Wrappers.lambdaQuery();
		deploymentLambdaQueryWrapper.eq(FlowDeployment::getLaneId, laneId);
		deploymentLambdaQueryWrapper.eq(FlowDeployment::getFlowId, flowId);
		FlowDeployment deploy = deploymentService.getOne(deploymentLambdaQueryWrapper);
		if (deploy != null && deploy.getVersion() == version) {
			throw new ServiceException("当前部署版本与实际流程版本保持一致，无需重新部署！");
		}

		BtpFlow flow = flowMapper.selectById(flowId);
		if (flow.getVersion() != version) {
			throw new ServiceException(String.format("部署版本与当前版本不符;当前版本：%s;部署版本%s", flow.getVersion(), version));
		}
		try {
			deploymentService.removeByLaneId(laneId);
		} catch (Exception e) {
			throw new ServiceException("清除原有部署信息失败");
		}

		JsonNode jsonNode = flow.getContent();
		JsonNode nodes = jsonNode.get("nodes");

		// 保存流程部署定义信息
		FlowDeployment deployment = FlowDeployment.builder() //
				.laneId(laneId)//
				.flowId(flowId)//
				.content(flow.getContent())//
				.version(version)//
				.createBy(SecurityUtils.getLoginUser().getUsername())//
				.createTime(new Date())//
				.build();
		deploymentService.save(deployment);

		List<FlowNode> flowNodeList = Lists.newArrayList();
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
					flowNodeList.add(flowNode);
				}
			});
		}

		List<FlowEdge> flowEdgeList = Lists.newArrayList();
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
					flowEdgeList.add(flowEdge);
				}

			});
		}
		return nodeService.saveBatch(flowNodeList) && edgeService.saveBatch(flowEdgeList);
	}

	public FlowNode nodeConvert(JsonNode jsonNode) {
		JsonNode properties = jsonNode.get("properties");
		FlowNode flowNode = FlowNode.builder() //
//				.deployId()
				.nodeId(jsonNode.get("id").asText())//
				.type(jsonNode.get("type").asText())//
				.content(jsonNode)//
				.build();
		flowNode = flowNode.setProperties(properties);
		return flowNode;
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

	public String getFieldValue(JsonNode jsonNode, String fieldName) {
		return jsonNode.hasNonNull(fieldName) ? jsonNode.get(fieldName).asText() : null;
	}
}
