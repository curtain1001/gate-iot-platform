package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowVariable;

/**
 * <p>
 * 流程实例变量 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Mapper
public interface FlowVariableMapper extends BaseMapper<FlowVariable> {

}
