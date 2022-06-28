package net.pingfang.device.plc;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.Unpooled;
import net.pingfang.device.core.DeviceInfo;
import net.pingfang.device.plc.codec.PlcDeviceMessageCodec;
import net.pingfang.iot.common.EncodedMessage;
import net.pingfang.iot.common.SimpleEncodedMessage;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:06
 */
public class PLCDevice implements DeviceInfo, ValueObject {
	final String deviceId;
	final String laneId;
	final String deviceCode;
	final String deviceName;
	final HashMap<String, Object> properties;
	final NetworkManager networkManager;
	final TcpClient tcpClient;
	final PlcDeviceMessageCodec messageCodec = new PlcDeviceMessageCodec();

	public PLCDevice(String deviceId, String laneId, String deviceCode, String deviceName,
			HashMap<String, Object> properties, NetworkManager networkManager) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceCode = deviceCode;
		this.deviceName = deviceName;
		this.properties = properties;
		this.networkManager = networkManager;
		tcpClient = (TcpClient) networkManager.getNetwork(DefaultNetworkType.TCP_CLIENT, deviceId);
	}

	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public String getLaneId() {
		return laneId;
	}

	@Override
	public String getDeviceCode() {
		return deviceCode;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public String getStatus() {
		return null;
	}

	@Override
	public void disconnect() {

	}

	@Override
	public Flux<EncodedMessage> subscribe() {
		return null;
	}

	@Override
	public Mono<Boolean> send(EncodedMessage message) {
		SimpleEncodedMessage encodedMessage = messageCodec.decode(message.getPayload());
		return tcpClient.send(new TcpMessage(Unpooled.wrappedBuffer(encodedMessage.getPayload())));
	}

	@Override
	public Map<String, Object> values() {
		return properties;
	}
}
