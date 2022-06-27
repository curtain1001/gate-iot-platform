package net.pingfang.iot.common;

/**
 * 消息报文类型
 */
public enum MessagePayloadType {

	JSON, STRING, BINARY, HEX, UNKNOWN;

	public static MessagePayloadType of(String of) {
		for (MessagePayloadType value : MessagePayloadType.values()) {
			if (value.name().equalsIgnoreCase(of)) {
				return value;
			}
		}
		return UNKNOWN;
	}
}
