package net.pingfang.device.plc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceState;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import reactor.core.publisher.Flux;

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

	public PLCDevice(Long laneId, String deviceId, String deviceName, TcpClient tcpClient) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.tcpClient = tcpClient;
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
	public void disconnect() {
		if (tcpClient != null) {
			tcpClient.disconnect();
		}
	}

	public Flux<FunctionMessage> subscribe() {
		return tcpClient.subscribe()
				.map(x -> new FunctionMessage(laneId, deviceId, null, x, MessagePayloadType.BINARY));
	}

	public void send(TcpMessage message) {
		tcpClient.send(message).block();
	}

	public DeviceState checkStatus() {
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
		byteBuf.writeBytes(new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF });
		TcpMessage tcpMessage = new TcpMessage();
		tcpMessage.setPayload(byteBuf);
		Boolean bln = tcpClient.send(tcpMessage).block();
		return bln != null && bln ? DeviceState.online : DeviceState.offline;
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
		if (tcpClient != null) {
			ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
			byteBuf.writeBytes(new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0xFF });
			TcpMessage tcpMessage = new TcpMessage();
			tcpMessage.setPayload(byteBuf);
			tcpClient.send(tcpMessage);
		}
	}

}
