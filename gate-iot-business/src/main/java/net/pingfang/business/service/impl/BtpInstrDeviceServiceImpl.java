package net.pingfang.business.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpInstrDevice;
import net.pingfang.business.manager.DefaultInstructionManager;
import net.pingfang.business.mapper.BtpInstrDeviceMapper;
import net.pingfang.business.service.IBtpInstrDeviceService;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description 设备命令关联信息服务实现类
 * @date 2022-06-29 0:17
 */
@Service
public class BtpInstrDeviceServiceImpl extends ServiceImpl<BtpInstrDeviceMapper, BtpInstrDevice>
		implements IBtpInstrDeviceService {
	@Resource
	DefaultInstructionManager instructionManager;

	@Override
	public List<Instruction> getInstructions(DeviceProduct deviceProduct) {
		return instructionManager.getInstruction(deviceProduct);
	}
}
