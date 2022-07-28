package net.pingfang.flow.core;

import net.pingfang.dockservice.core.BusinessInstruction;
import net.pingfang.iot.common.instruction.InstructionResult;

/**
 * @author 王超
 * @description
 * @date 2022-07-29 0:33
 */
public class ServerTaskNode implements TaskNode {

	private Long laneId;
	/**
	 * 节点代码
	 */
	private String nodeCode;

	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 执行指令
	 */
	private BusinessInstruction instruction;
	/**
	 * 条件脚本
	 */
	private String conditionScript;

	@Override
	public Long getLaneId() {
		return this.laneId;
	}

	@Override
	public String getNodeCode() {
		return this.nodeCode;
	}

	@Override
	public String getNodeName() {
		return this.nodeName;
	}

	@Override
	public BusinessInstruction getInstruction() {
		return this.instruction;
	}

	@Override
	public String getConditionScript() {
		return this.conditionScript;
	}

	public InstructionResult device() {
		return instruction.execution();
	}
}
