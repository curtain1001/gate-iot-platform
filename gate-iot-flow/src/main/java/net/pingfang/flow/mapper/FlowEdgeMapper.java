package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowEdge;

/**
 * <p>
 * 流程节点连线信息
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 14:57
 */
@Mapper
public interface FlowEdgeMapper extends BaseMapper<FlowEdge> {
}
