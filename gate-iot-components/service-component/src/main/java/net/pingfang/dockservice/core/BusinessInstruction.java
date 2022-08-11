package net.pingfang.dockservice.core;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 10:59
 */
public interface BusinessInstruction extends Instruction {
	/**
	 *
	 * @param jsonNode fieldName 节点名称；fieldValue: 节点执行报文
	 * @return
	 */
	public abstract InstructionResult execution(JsonNode jsonNode);

	@Override
	default ObjectType getObjectType() {
		return ObjectType.service;
	}

}
