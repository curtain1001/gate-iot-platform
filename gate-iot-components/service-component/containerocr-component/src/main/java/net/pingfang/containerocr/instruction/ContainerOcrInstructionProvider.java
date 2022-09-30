package net.pingfang.containerocr.instruction;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.manager.InstructionConfigManager;
import net.pingfang.iot.common.product.Product;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 14:06
 */
@Component
public class ContainerOcrInstructionProvider implements InstructionProvider {
	@Resource
	InstructionConfigManager instructionConfigManager;

	@Override
	public String getName() {
		return ContainerOcrProduct.CONTAINER_OCR.getValue();
	}

	@Override
	public List<Instruction> getCommand() {
		List<InsEntity> entities = instructionConfigManager.getInstruction(ContainerOcrProduct.CONTAINER_OCR);
		if (CollectionUtils.isNotEmpty(entities)) {
			List<Instruction> instructions = entities.stream().map(x -> new BusinessInstruction() {
				@Override
				public InstructionResult execution(JsonNode jsonNode) {
					return null;
				}

				@Override
				public String getName() {
					return x.getName();

				}

				@Override
				public String getValue() {
					return x.getValue();
				}

				@Override
				public Product getProduct() {
					return ContainerOcrProduct.CONTAINER_OCR;
				}

				@Override
				public InstructionType getInsType() {
					return InstructionType.valueOf(x.getType());
				}
			}).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(instructions)) {
				instructions.add(new OrcInstruction());
				return instructions;
			}
		}
		return Collections.singletonList(new OrcInstruction());
	}
}
