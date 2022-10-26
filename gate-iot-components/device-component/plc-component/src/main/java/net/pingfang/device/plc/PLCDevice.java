package net.pingfang.device.plc;

import java.util.Date;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.config.RabbitMQConfig;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.message.DeviceCollectMessage;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.client.TcpClientNetworkType;
import net.pingfang.network.utils.BytesUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:06
 */
@Slf4j
public class PLCDevice implements DeviceOperator {
	final String deviceId;
	final Long laneId;
	final String deviceName;
	private TcpClient tcpClient;
	final NetworkManager networkManager;
	final InstructionManager instructionManager;
	final RabbitTemplate rabbitTemplate;

	public PLCDevice(Long laneId, String deviceId, String deviceName, NetworkManager networkManager,
			InstructionManager instructionManager, RabbitTemplate rabbitTemplate) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.networkManager = networkManager;
		this.instructionManager = instructionManager;
		this.rabbitTemplate = rabbitTemplate;
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
	public DeviceProduct getProduct() {
		return PLCDeviceProduct.PLC;
	}

	@Override
	public void shutdown() {
		networkManager.shutdown(TcpClientNetworkType.TCP_CLIENT, deviceId);
		this.tcpClient = null;
	}

	@Override
	public Flux<FunctionMessage> subscribe() {
		return tcpClient.subscribe() //
				.map(x -> new FunctionMessage(this.laneId, deviceId, PLCDeviceProduct.PLC, //
						BytesUtils.getBufHexStr(ByteBufUtil.getBytes((ByteBuf) x.getPayload())),
						MessagePayloadType.STRING, null));
	}

	public Mono<Boolean> send(TcpMessage message) {
		return tcpClient.send(message);
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
		this.tcpClient.subscribe().subscribe(x -> {
			try {
				DeviceCollectMessage message = new DeviceCollectMessage(x.getDeviceId(), x.getLaneId(),
						x.payloadAsString(), new Date());
				rabbitTemplate.convertAndSend(RabbitMQConfig.TOPIC_EXCHANGE_DEVICE_MSG, "topic.device." + getProduct(),
						message);
			} catch (Exception e) {
				log.error("PLC处理订阅消息失败：", e);
			}

		});
	}
}
