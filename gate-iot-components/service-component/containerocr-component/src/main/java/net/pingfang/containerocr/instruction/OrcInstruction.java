package net.pingfang.containerocr.instruction;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * <p>
 * 箱号识别指令
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 14:09
 */
public class OrcInstruction implements BusinessInstruction {
	@Override
	public String getName() {
		return "箱号识别";
	}

	@Override
	public String getValue() {
		return "CONTAINER_OCR";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.up;
	}

	@Override
	public ObjectType getObjectType() {
		return BusinessInstruction.super.getObjectType();
	}

	@Override
	public boolean isSupport(Object object) {
		return BusinessInstruction.super.isSupport(object);
	}

	@Override
	public InstructionResult execution(JsonNode jsonNode) {
		return null;
	}
}
