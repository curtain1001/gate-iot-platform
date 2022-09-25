package net.pingfang.network.tcp.server;

import net.pingfang.network.Network;
import net.pingfang.network.NetworkMessage;
import reactor.core.publisher.Flux;

/**
 * TCP服务
 *
 * @author zhouhao
 * @version 1.0
 **/
public interface TcpServer extends Network {

	Flux<NetworkMessage> subscribe();

	/**
	 * 关闭服务端
	 */
	@Override
	void shutdown();
}
