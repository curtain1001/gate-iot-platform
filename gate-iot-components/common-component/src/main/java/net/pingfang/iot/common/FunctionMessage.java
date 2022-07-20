package net.pingfang.iot.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 9:40
 */
@Data
@AllArgsConstructor
@Builder
public class FunctionMessage {
	/**
	 * 车道id
	 */
	final Long laneId;
	/**
	 * 设备id
	 */
	final String deviceId;

	/**
	 * 指令对象
	 */
	final String command;

	/**
	 * 执行结果
	 */
	final Object Payload;

	final MessagePayloadType type;

}
