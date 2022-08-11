package net.pingfang.business.service.impl;

import java.util.Date;

import javax.annotation.Resource;

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
import net.pingfang.flow.service.IFlowDeploymentService;
import net.pingfang.flow.service.IFlowEdgeService;
import net.pingfang.flow.service.IFlowNodeService;
import net.pingfang.iot.common.instruction.InstructionManager;

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
	@Resource
	public InstructionManager instructionManager;

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
		return deploymentService.save(deployment);
	}

	public String getFieldValue(JsonNode jsonNode, String fieldName) {
		return jsonNode.hasNonNull(fieldName) ? jsonNode.get(fieldName).asText() : null;
	}
}
