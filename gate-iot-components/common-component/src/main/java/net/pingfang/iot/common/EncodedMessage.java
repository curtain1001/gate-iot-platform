package net.pingfang.iot.common;

import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 已编码的消息,通常为来自设备或者发向设备的原始报文.
 *
 * @author 王超
 * @since 1.0.0
 */
public interface EncodedMessage {

	/**
	 * 获取原始报文
	 *
	 * @return ByteBuf
	 */
	ByteBuf getPayload();

	default String payloadAsString() {
		return getPayload().toString(StandardCharsets.UTF_8);
	}

	default JSONObject payloadAsJson() {
		return (JSONObject) JSON.parse(payloadAsBytes());
	}

	default JSONArray payloadAsJsonArray() {
		return (JSONArray) JSON.parse(payloadAsBytes());
	}

	default byte[] payloadAsBytes() {
		return ByteBufUtil.getBytes(getPayload());
	}

	@Deprecated
	default byte[] getBytes() {
		return ByteBufUtil.getBytes(getPayload());
	}

	default byte[] getBytes(int offset, int len) {
		return ByteBufUtil.getBytes(getPayload(), offset, len);
	}

	static EmptyMessage empty() {
		return EmptyMessage.INSTANCE;
	}

	static EncodedMessage simple(ByteBuf data) {
		return simple(data, MessagePayloadType.BINARY);
	}

	static EncodedMessage simple(ByteBuf data, MessagePayloadType payloadType) {
		return SimpleEncodedMessage.of(data, payloadType);
	}

}
