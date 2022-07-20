package net.pingfang.device.plc.codec;

import io.netty.buffer.ByteBuf;
import net.pingfang.device.core.codec.DeviceMessageCodec;
import net.pingfang.iot.common.EncodedMessage;
import net.pingfang.iot.common.SimpleEncodedMessage;

/**
 * @author 王超
 * @description PLC 设备编解码器
 * @date 2022-06-29 1:01
 */
public class PlcDeviceMessageCodec implements DeviceMessageCodec {
	@Override
	public EncodedMessage encode(ByteBuf obj) {
		return null;
	}

	@Override
	public SimpleEncodedMessage decode(ByteBuf obj) {
		return null;
	}
}
