package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowVariable;
import net.pingfang.flow.mapper.FlowVariableMapper;
import net.pingfang.flow.service.IFlowVariableService;

/**
 * <p>
 * 流程实例变量 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowVariableServiceImpl extends ServiceImpl<FlowVariableMapper, FlowVariable>
		implements IFlowVariableService {

}
