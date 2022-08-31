package net.pingfang.network.tcp.server;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 8:42
 */
@Slf4j
public class TcpServerMessageHandler {
	final TcpServer tcpServer;

	public TcpServerMessageHandler(TcpServer tcpServer) {
		this.tcpServer = tcpServer;
	}

	public void handler() {
		this.tcpServer.handleConnection().flatMap(TcpClient::subscribe).map(TcpMessage::getPayload).subscribe(x -> {
			log.info("tcpServer:{}", x);
		});
	}
}
