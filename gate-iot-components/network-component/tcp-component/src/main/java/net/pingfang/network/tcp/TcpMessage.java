package net.pingfang.network.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.EncodedMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-24 17:32
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcpMessage implements EncodedMessage {

	private ByteBuf payload;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		ByteBufUtil.appendPrettyHexDump(builder, payload);

		return builder.toString();
	}
}
