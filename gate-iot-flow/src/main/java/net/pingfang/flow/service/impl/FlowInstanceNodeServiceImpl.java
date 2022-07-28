package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowInstanceNode;
import net.pingfang.flow.mapper.FlowInstanceNodeMapper;
import net.pingfang.flow.service.IFlowInstanceNodeService;

/**
 * <p>
 * 流程节点表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowInstanceNodeServiceImpl extends ServiceImpl<FlowInstanceNodeMapper, FlowInstanceNode>
		implements IFlowInstanceNodeService {

}
