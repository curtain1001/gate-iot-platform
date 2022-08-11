package net.pingfang.device.plc;

import java.util.Collections;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceState;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.parser.PayloadParserType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:06
 */
public class PLCDevice implements DeviceOperator {
	final String deviceId;
	final Long laneId;
	final String deviceName;
	private TcpClient tcpClient;
	final NetworkManager networkManager;

	public PLCDevice(Long laneId, String deviceId, String deviceName, NetworkManager networkManager) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.networkManager = networkManager;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public Long getLaneId() {
		return laneId;
	}

	@Override
	public String getDeviceName() {
		return deviceName;
	}

	@Override
	public Product getProduct() {
		return PLCProduct.PLC;
	}

	@Override
	public DeviceState getStatus() {
		return checkStatus();
	}

	@Override
	public void shutdown() {
		networkManager.shutdown(DefaultNetworkType.TCP_CLIENT, deviceId);
		this.tcpClient = null;
	}

	@Override
	public Flux<FunctionMessage> subscribe() {
		return tcpClient.subscribe()
				.map(x -> new FunctionMessage(laneId, deviceId, PLCProduct.PLC, x, MessagePayloadType.BINARY));
	}

	public Mono<Boolean> send(TcpMessage message) {
		return tcpClient.send(message);
	}

	public DeviceState checkStatus() {
		if (isAlive()) {
			ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
			byteBuf.writeBytes(new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF });
			TcpMessage tcpMessage = new TcpMessage();
			tcpMessage.setPayload(byteBuf);
			Boolean bln = tcpClient.send(tcpMessage).block();
			return bln != null && bln ? DeviceState.online : DeviceState.offline;
		} else {
			return DeviceState.offline;
		}

	}

	@Override
	public void setStatus(DeviceState state) {

	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	@Override
	public boolean isAlive() {
		if (tcpClient != null) {
			return tcpClient.isAlive();
		}
		return false;
	}

	@Override
	public void keepAlive() {
		if (isAlive()) {
			ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
			byteBuf.writeBytes(new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF });
			TcpMessage tcpMessage = new TcpMessage();
			tcpMessage.setPayload(byteBuf);
			tcpClient.send(tcpMessage);
		}
	}

	public void setTcpClient(Map<String, Object> properties) {
		// 粘黏包处理设置
		properties.put("parserType", PayloadParserType.FIXED_LENGTH);
		properties.put("parserConfiguration", Collections.singletonMap("size", 4));

		NetworkProperties networkProperties = new NetworkProperties();
		networkProperties.setId(deviceId);
		networkProperties.setName("PLC::TCP::CLIENT::" + deviceId);
		networkProperties.setEnabled(true);
		networkProperties.setConfigurations(properties);
		this.tcpClient = (TcpClient) networkManager.getNetwork(DefaultNetworkType.TCP_CLIENT, networkProperties,
				deviceId);
	}

}
