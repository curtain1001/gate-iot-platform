package net.pingfang.flow.core;

import lombok.Data;
import net.pingfang.iot.common.instruction.Instruction;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 16:35
 */
@Data
public class Task {
	Long laneId;
	Instruction instruction;
	String currentState;
}
