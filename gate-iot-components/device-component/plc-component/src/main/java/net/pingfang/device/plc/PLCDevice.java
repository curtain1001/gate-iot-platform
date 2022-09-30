package net.pingfang.device.plc;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceState;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.utils.BytesUtils;
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
	final InstructionManager instructionManager;

	public PLCDevice(Long laneId, String deviceId, String deviceName, NetworkManager networkManager,
			InstructionManager instructionManager) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.networkManager = networkManager;
		this.instructionManager = instructionManager;
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
	public void shutdown() {
		networkManager.shutdown(DefaultNetworkType.TCP_CLIENT, deviceId);
		this.tcpClient = null;
	}

	@Override
	public Flux<FunctionMessage> subscribe(Long laneId) {
		return tcpClient.subscribe() //
				.map(x -> new FunctionMessage(this.laneId, deviceId, PLCProduct.PLC, //
						BytesUtils.getBufHexStr(ByteBufUtil.getBytes((ByteBuf) x.getPayload())),
						MessagePayloadType.STRING, null)) //
				.filterWhen(x -> {
					if (laneId != null) {
						return Mono.just(laneId.equals(x.getLaneId()));
					} else {
						return Mono.just(true);
					}
				});
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

	public void setTcpClient(TcpClient tcpClient) {
		this.tcpClient = tcpClient;
	}
}
