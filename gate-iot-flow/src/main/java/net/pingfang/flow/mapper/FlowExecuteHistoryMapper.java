package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowExecuteHistory;

/**
 * <p>
 * 流程执行历史
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 14:24
 */
@Mapper
public interface FlowExecuteHistoryMapper extends BaseMapper<FlowExecuteHistory> {
}
