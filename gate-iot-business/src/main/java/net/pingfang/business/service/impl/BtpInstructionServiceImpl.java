package net.pingfang.business.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpInstruction;
import net.pingfang.business.manager.DefaultInstructionManager;
import net.pingfang.business.mapper.BtpInstructionMapper;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 23:42
 */
@Service
public class BtpInstructionServiceImpl extends ServiceImpl<BtpInstructionMapper, BtpInstruction>
		implements IBtpInstructionService {
	@Resource
	DefaultInstructionManager instructionManager;

	@Override
	public List<Instruction> getInstructions(DeviceProduct deviceProduct) {
		return instructionManager.getInstruction(deviceProduct);
	}
}
