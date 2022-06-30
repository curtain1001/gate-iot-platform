package net.pingfang.device.plc;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.Unpooled;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.plc.codec.PlcDeviceMessageCodec;
import net.pingfang.iot.common.EncodedMessage;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:06
 */
public class PLCDevice implements DeviceOperator, ValueObject {
	final Long deviceId;
	final String laneId;
	final String deviceCode;
	final String deviceName;
	final HashMap<String, Object> properties;
	final TcpClient tcpClient;
	final PlcDeviceMessageCodec messageCodec = new PlcDeviceMessageCodec();

	public PLCDevice(Long deviceId, String laneId, String deviceCode, String deviceName,
			HashMap<String, Object> properties, TcpClient tcpClient) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceCode = deviceCode;
		this.deviceName = deviceName;
		this.properties = properties;
		this.tcpClient = tcpClient;
	}

	@Override
	public HashMap<String, Object> getProperties() {
		return properties;
	}

	@Override
	public Long getDeviceId() {
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
		tcpClient.disconnect();
	}

	@Override
	public Flux<EncodedMessage> subscribe() {
		return null;
	}

	@Override
	public Mono<Boolean> send(EncodedMessage message) {
//		SimpleEncodedMessage encodedMessage = messageCodec.decode(message.getPayload());
		return tcpClient.send(new TcpMessage(Unpooled.wrappedBuffer(message.getPayload())));
	}

	@Override
	public Map<String, Object> values() {
		return properties;
	}

}
