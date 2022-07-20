package net.pingfang.dockservice.customs.instruction;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.dockservice.core.BusinessInstruction;
import net.pingfang.iot.common.instruction.InstructionType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 10:31
 */
@Slf4j
public enum CustomsInstruction implements BusinessInstruction {
	DOCK_CUSTOMS;

	@Override
	public String getName() {
		return "对接海关";
	}

	@Override
	public String getValue() {
		return this.name();
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.down;
	}

	@Override
	public void execution() {
		log.info("对接海关");
	}

}
