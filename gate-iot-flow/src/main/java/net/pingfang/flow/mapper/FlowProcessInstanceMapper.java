package net.pingfang.flow.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.flow.domain.FlowProcessInstance;

/**
 * <p>
 * 流程实例表 Mapper 接口
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Mapper
public interface FlowProcessInstanceMapper extends BaseMapper<FlowProcessInstance> {
}
