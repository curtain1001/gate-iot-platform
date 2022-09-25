package net.pingfang.flow.values;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.Product;

/**
 * <p>
 * 流程消息
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 16:07
 */
@Data
@Builder
public class ProcessMessage {
	/**
	 * 车道id
	 */
	private final Long laneId;
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
	 * 报文类型
	 */
	private final MessagePayloadType type;
	/**
	 * 消息id
	 */
	private final String messageId;
	/**
	 * 报文内容
	 */
	private final Object message;
}
