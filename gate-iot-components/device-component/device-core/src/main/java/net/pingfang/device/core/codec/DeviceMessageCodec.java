package net.pingfang.device.core.codec;

import io.netty.buffer.ByteBuf;
import net.pingfang.iot.common.EncodedMessage;
import net.pingfang.iot.common.SimpleEncodedMessage;

/**
 * @author 王超
 * @description 设备消息解编码器
 * @date 2022-06-29 0:47
 */
public interface DeviceMessageCodec {
	/**
	 * 编码器
	 *
	 * @param obj
	 * @return 编码报文
	 */
	EncodedMessage encode(ByteBuf obj);

	/**
	 * 解码器
	 */
	SimpleEncodedMessage decode(ByteBuf obj);
}
