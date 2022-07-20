package net.pingfang.iot.common.instruction;

import java.util.Map;

/**
 * @author 王超
 * @description 指令执行上下文
 * @date 2022-07-13 11:35
 */
public class InstructionContext {
	/**
	 * 车道id
	 */
	public Long laneId;
	/**
	 * 属性《设备id，报文内容》
	 */
	public Map<String, Object> content;

	/**
	 * 执行结果
	 */
	/**
	 * 属性《设备id，执行结果》
	 */
	public Map<String, Boolean> result;
}
