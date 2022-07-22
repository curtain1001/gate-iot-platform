package net.pingfang.iot.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.product.Product;

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

	final Product product;

	/**
	 * 执行结果
	 */
	final Object Payload;

	final MessagePayloadType type;

}
