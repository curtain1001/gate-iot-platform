package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowExecuteHistory;
import net.pingfang.flow.mapper.FlowExecuteHistoryMapper;
import net.pingfang.flow.service.IFlowExecuteHistoryService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 14:25
 */
@Service
public class FlowExecuteHistoryServiceImpl extends ServiceImpl<FlowExecuteHistoryMapper, FlowExecuteHistory>
		implements IFlowExecuteHistoryService {
}
