package net.pingfang.flow.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.pingfang.flow.mapper.FlowInstanceMapper;
import net.pingfang.flow.service.IFlowInstanceNodeService;
import net.pingfang.flow.service.IFlowService;

/**
 * 流程接口实现
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowServiceImpl implements IFlowService {

	@Resource
	private IFlowInstanceNodeService instanceNodeService;

	@Resource
	private FlowInstanceMapper instanceMapper;

	@Override
	public void success(String taskCode, String note) {

	}

	@Override
	public void fail(String taskCode, String note) {
	}

}
