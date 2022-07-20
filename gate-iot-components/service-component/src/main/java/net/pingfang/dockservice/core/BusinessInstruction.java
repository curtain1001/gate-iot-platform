package net.pingfang.dockservice.core;

import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 10:59
 */
public interface BusinessInstruction extends Instruction {

	public abstract void execution();

	@Override
	default ObjectType getObjectType() {
		return ObjectType.service;
	}

}
