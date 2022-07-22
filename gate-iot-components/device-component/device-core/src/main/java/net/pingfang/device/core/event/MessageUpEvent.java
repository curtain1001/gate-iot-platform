package net.pingfang.device.core.event;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description 设备消息上行事件
 * @date 2022-07-07 10:10
 */
@Data
@Builder(toBuilder = true)
public class MessageUpEvent {
	/**
	 * 车道id
	 */
	private final long laneId;
	/**
	 * 设备id
	 */
	private final String deviceId;
	/**
	 * 设备产品类型
	 */
	private final Product product;
	/**
	 * 指令
	 */
	private final Instruction instruction;
	/**
	 *
	 */
	private final String type;
	/**
	 * 消息id
	 */
	private final String messageId;
	/**
	 * 报文内容
	 */
	private final Object message;
}
