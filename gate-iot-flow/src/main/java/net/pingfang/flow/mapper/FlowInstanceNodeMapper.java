package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowInstanceNode;

/**
 * <p>
 * 流程节点表 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Mapper
public interface FlowInstanceNodeMapper extends BaseMapper<FlowInstanceNode> {

}
