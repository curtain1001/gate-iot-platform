package net.pingfang.flow.core;

import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionType;

/**
 * @author 王超
 * @since 2022-07-28 17:37
 */
public interface TaskNode {

	Long getLaneId();

	/**
	 * 节点代码
	 */
	String getNodeCode();

	/**
	 * 节点名称
	 */
	String getNodeName();

	/**
	 * 执行指令
	 */
	Instruction getInstruction();

	/**
	 * 条件脚本
	 */
	String getConditionScript();


	/**
	 * 流转判断
	 *
	 * @return 是否流转
	 */
	default boolean circulation(String event) {
		if (getInstruction().getInsType().equals(InstructionType.up)) {
			return getInstruction().getValue().equals(event);
		} else {
			return true;
		}
	}
}
