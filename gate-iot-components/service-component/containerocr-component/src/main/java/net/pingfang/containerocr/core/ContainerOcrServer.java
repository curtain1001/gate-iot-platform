package net.pingfang.containerocr.core;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.server.TcpServer;
import net.pingfang.servicecomponent.core.ServerOperator;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 11:29
 */

@Data
@Slf4j
public class ContainerOcrServer implements ServerOperator {
	final Long laneId;
	final String serverId;
	final String serverName;
	final NetworkManager networkManager;
	final InstructionManager instructionManager;

	public ContainerOcrServer(Long laneId, String serverId, String serverName, NetworkManager networkManager,
			InstructionManager instructionManager) {
		this.serverId = serverId;
		this.laneId = laneId;
		this.serverName = serverName;
		this.networkManager = networkManager;
		this.instructionManager = instructionManager;

	}

	@Override
	public String getServerId() {
		return serverId;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public Long getLaneId() {
		return this.laneId;
	}

	@Override
	public Product getProduct() {
		return ContainerOcrProduct.CONTAINER_OCR;
	}

	@Override
	public void close() {
		TcpServer tcpServer = (TcpServer) networkManager.getNetwork(DefaultNetworkType.TCP_SERVER, serverId);
		if (tcpServer != null) {
			tcpServer.shutdown();
		}
	}

	@Override
	public Flux<FunctionMessage> subscribe(Long laneId) {
		TcpServer tcpServer = (TcpServer) networkManager.getNetwork(DefaultNetworkType.TCP_SERVER, serverId);
		if (tcpServer != null) {
			return tcpServer.handleConnection().flatMap(TcpClient::subscribe).map(x -> new FunctionMessage(laneId, null,
					ContainerOcrProduct.CONTAINER_OCR, x, MessagePayloadType.BINARY));
		} else {
			throw new RuntimeException("服务异常：网络组件未正常启动！");
		}
	}

	@Override
	public boolean isAlive() {
		TcpServer tcpServer = (TcpServer) networkManager.getNetwork(DefaultNetworkType.TCP_SERVER, serverId);
		if (tcpServer != null) {
			return tcpServer.isAlive();
		}
		return false;
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

}
