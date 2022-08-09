package net.pingfang.flow.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.common.exception.ServiceException;
import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.domain.FlowProcessInstance;
import net.pingfang.flow.mapper.FlowProcessInstanceMapper;
import net.pingfang.flow.service.IFlowProcessInstanceService;

/**
 * <p>
 * 流程实例表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowProcessInstanceServiceImpl extends ServiceImpl<FlowProcessInstanceMapper, FlowProcessInstance>
		implements IFlowProcessInstanceService {

	@Resource
	FlowProcessInstanceMapper instanceMapper;

	/**
	 * 获取下一个节点
	 *
	 * @param instanceId
	 * @return
	 */
	@Override
	public FlowNode getNextNode(Long instanceId) {
		FlowProcessInstance instance = instanceMapper.selectById(instanceId);
		if (instance == null || !instance.getStatus().inProgress()) {
			throw new ServiceException("该流程已结束");
		}

		return null;
	}
}
