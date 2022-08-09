package net.pingfang.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.flow.domain.FlowDeployment;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-08 15:40
 */
public interface IFlowDeploymentService extends IService<FlowDeployment> {

	int removeByLaneId(Long laneId);
}
