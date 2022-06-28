package net.pingfang.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.business.domain.BtpInstruction;

/**
 * @author 王超
 * @description 指令
 * @date 2022-06-28 23:41
 */
@Mapper
public interface BtpInstructionMapper extends BaseMapper<BtpInstruction> {
}
