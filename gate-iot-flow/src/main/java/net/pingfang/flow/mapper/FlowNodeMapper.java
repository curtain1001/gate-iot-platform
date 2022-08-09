package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowNode;

/**
 * <p>
 * 流程节点信息
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 14:57
 */
@Mapper
public interface FlowNodeMapper extends BaseMapper<FlowNode> {
}
