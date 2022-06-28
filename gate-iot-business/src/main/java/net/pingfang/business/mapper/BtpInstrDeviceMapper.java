package net.pingfang.business.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.pingfang.business.domain.BtpInstrDevice;

/**
 * @author 王超
 * @description 设备命令关联信息
 * @date 2022-06-29 0:14
 */
@Mapper
public interface BtpInstrDeviceMapper extends BaseMapper<BtpInstrDevice> {
}
