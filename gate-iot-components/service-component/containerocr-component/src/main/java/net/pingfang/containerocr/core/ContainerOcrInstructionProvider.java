package net.pingfang.containerocr.core;

import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;

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
	@Override
	public String getName() {
		return ContainerOcrProduct.CONTAINER_OCR.getValue();
	}

	@Override
	public List<Instruction> getCommand() {
		return Lists.newArrayList();
	}
}
