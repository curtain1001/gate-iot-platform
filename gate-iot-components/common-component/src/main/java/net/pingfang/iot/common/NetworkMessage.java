package net.pingfang.iot.common;

import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.common.utils.ByteUtils;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.iot.common.network.NetworkType;

/**
 * @author bsetfeng
 * @author zhouhao
 * @since 1.0
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class NetworkMessage {

	private Object payload;

	private MessagePayloadType payloadType;

	private String deviceId;

	private Long laneId;

	private NetworkType networkType;

	public Object getPayload() {
		return payload;
	}

	public String payloadAsString() {
		if (payloadType == MessagePayloadType.BINARY) {
			return new String((byte[]) payload, StandardCharsets.UTF_8);
		} else if (payloadType == MessagePayloadType.STRING) {

			return (String) payload;
		} else if (payloadType == MessagePayloadType.JSON) {

			return JsonUtils.toJsonString(payload);
		} else if (payloadType == MessagePayloadType.HEX) {

			return ByteUtils.hexStringToString((String) payload);
		} else {
			return (String) payload;
		}
	}

	public byte[] getBytes() {
		if (payloadType == MessagePayloadType.BINARY) {
			return (byte[]) payload;
		} else if (payloadType == MessagePayloadType.STRING) {
			return ((String) payload).getBytes(StandardCharsets.UTF_8);
		} else if (payloadType == MessagePayloadType.JSON) {
			return JsonUtils.toJsonBytes(payload);
		} else if (payloadType == MessagePayloadType.HEX) {
			return ByteUtils.hexToByteArray((String) payload);
		} else {
			return (byte[]) payload;
		}
	}

}
