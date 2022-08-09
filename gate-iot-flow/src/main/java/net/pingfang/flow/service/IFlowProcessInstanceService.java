package net.pingfang.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.domain.FlowProcessInstance;

/**
 * <p>
 * 流程实例表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
public interface IFlowProcessInstanceService extends IService<FlowProcessInstance> {

	public FlowNode getNextNode(Long instanceId);

}
