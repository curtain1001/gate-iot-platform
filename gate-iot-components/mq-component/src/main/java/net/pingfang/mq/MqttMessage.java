package net.pingfang.mq;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.pingfang.iot.common.EncodedMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 14:07
 */
@Data
public class MqttMessage implements EncodedMessage {
	final ByteBuf byteBuf;

	@Override
	public ByteBuf getPayload() {
		return byteBuf;
	}
}
