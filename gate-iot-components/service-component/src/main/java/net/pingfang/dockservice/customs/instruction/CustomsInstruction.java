package net.pingfang.dockservice.customs.instruction;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.dockservice.core.BusinessInstruction;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 10:31
 */
@Slf4j
public class CustomsInstruction implements BusinessInstruction {

	@Override
	public String getName() {
		return "对接海关";
	}

	@Override
	public String getValue() {
		return "DOCK_CUSTOMS";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.down;
	}

	@Override
	public InstructionResult<String, String> execution(JsonNode jsonNode) {
		log.info("对接海关");
		return InstructionResult.success(null, "执行成功");
	}

}
