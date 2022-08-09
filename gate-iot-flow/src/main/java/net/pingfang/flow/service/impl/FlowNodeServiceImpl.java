package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowNode;
import net.pingfang.flow.mapper.FlowNodeMapper;
import net.pingfang.flow.service.IFlowNodeService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 14:59
 */
@Service
public class FlowNodeServiceImpl extends ServiceImpl<FlowNodeMapper, FlowNode> implements IFlowNodeService {
}
