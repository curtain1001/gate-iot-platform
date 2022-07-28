package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowInstance;
import net.pingfang.flow.mapper.FlowInstanceMapper;
import net.pingfang.flow.service.IFlowInstanceService;

/**
 * <p>
 * 流程实例表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowInstanceServiceImpl extends ServiceImpl<FlowInstanceMapper, FlowInstance>
		implements IFlowInstanceService {

}
