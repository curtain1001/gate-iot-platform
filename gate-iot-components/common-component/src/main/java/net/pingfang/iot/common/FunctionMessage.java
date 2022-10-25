package net.pingfang.iot.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.DeviceProduct;

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
	 * 服务
	 */
	final DeviceProduct deviceProduct;
	/**
	 * 执行结果
	 */
	final Object Payload;

	final MessagePayloadType type;

	final Instruction instruction;

}
