package net.pingfang.business.manager;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpInstruction;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.iot.common.instruction.FormatType;
import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.manager.InstructionConfigManager;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 11:45
 */
@Component
public class DefaultInstructionConfigManager implements InstructionConfigManager {
	@Resource
	public IBtpInstructionService instructionService;

	@Override
	public List<InsEntity> getInstruction(DeviceProduct deviceProduct) {
		LambdaQueryWrapper<BtpInstruction> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpInstruction::getProduct, deviceProduct.getValue());
		queryWrapper.eq(BtpInstruction::getStatus, 0);
		List<BtpInstruction> instructions = instructionService.list(queryWrapper);
		return instructions.stream().map(instruction -> InsEntity.builder() //
				.name(instruction.getCommandName())//
				.value(instruction.getCommandValue())//
				.content(instruction.getContent())//
				.format(FormatType.of(instruction.getFormat()))//
				.product(instruction.getProduct())//
				.type(instruction.getType())//
				.build()) //
				.collect(Collectors.toList());
	}
}
