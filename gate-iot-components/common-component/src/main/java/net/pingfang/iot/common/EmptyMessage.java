package net.pingfang.iot.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * 空消息
 *
 * @author 王超
 * @since 1.0.0
 */
public final class EmptyMessage implements EncodedMessage {

	public static final EmptyMessage INSTANCE = new EmptyMessage();

	private EmptyMessage() {
	}

	@Override
	public ByteBuf getPayload() {
		return Unpooled.wrappedBuffer(new byte[0]);
	}

	@Override
	public String toString() {
		return "empty message";
	}
}
