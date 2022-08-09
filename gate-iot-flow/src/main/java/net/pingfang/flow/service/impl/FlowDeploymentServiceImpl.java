package net.pingfang.flow.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.common.utils.SecurityUtils;
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.mapper.FlowDeploymentMapper;
import net.pingfang.flow.service.IFlowDeploymentService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-08 15:41
 */
@Service
public class FlowDeploymentServiceImpl extends ServiceImpl<FlowDeploymentMapper, FlowDeployment>
		implements IFlowDeploymentService {

	@Resource
	FlowDeploymentMapper flowDeploymentMapper;

	@Override
	public int removeByLaneId(Long laneId) {
		LambdaUpdateWrapper<FlowDeployment> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(FlowDeployment::getLaneId, laneId);
		updateWrapper.set(FlowDeployment::getDeleted, 1);
		updateWrapper.set(FlowDeployment::getUpdateBy, SecurityUtils.getUsername());
		updateWrapper.set(FlowDeployment::getUpdateTime, new Date());
		return flowDeploymentMapper.update(FlowDeployment.builder().build(), updateWrapper);
	}
}
