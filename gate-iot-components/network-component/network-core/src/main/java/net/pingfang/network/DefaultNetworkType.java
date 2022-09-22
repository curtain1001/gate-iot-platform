package net.pingfang.network;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;

@AllArgsConstructor
@Getter
public enum DefaultNetworkType implements NetworkType {

	TCP_CLIENT("TCP客户端"), //
	TCP_SERVER("TCP服务"), //

	MQTT_CLIENT("MQTT客户端"), //
	MQTT_SERVER("MQTT服务"), //

	HTTP_CLIENT("HTTP客户端"), //
	HTTP_SERVER("HTTP服务"), //

	WEB_SOCKET_CLIENT("WebSocket客户端"), //
	WEB_SOCKET_SERVER("WebSocket服务"), //

	UDP("UDP"), //

	COAP_CLIENT("CoAP客户端"), //
	COAP_SERVER("CoAP服务"), //
	INTERNAL("内置服务"),;

	static {
		NetworkTypes.register(Arrays.asList(DefaultNetworkType.values()));
	}

	private final String name;

	@Override
	public String getId() {
		return name();
	}
}
