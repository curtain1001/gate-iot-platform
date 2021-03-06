package net.pingfang.flow.core;

import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.iot.common.instruction.InstructionResult;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-07-28 17:37
 */
public class DeviceTaskNode implements TaskNode {

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
	private DeviceInstruction instruction;
	/**
	 * 条件脚本
	 */
	private String conditionScript;

	private String deviceId;

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
	public DeviceInstruction getInstruction() {
		return this.instruction;
	}

	@Override
	public String getConditionScript() {
		return this.conditionScript;
	}

	public String getDeviceId() {
		return this.deviceId;
	}

	public InstructionResult device(DeviceManager deviceManager) {
		DeviceOperator operator = deviceManager.getDevice(laneId, deviceId);
		return instruction.execution(operator);
	}
}
