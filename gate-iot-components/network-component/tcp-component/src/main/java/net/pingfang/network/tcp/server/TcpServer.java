package net.pingfang.network.tcp.server;

import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.network.Network;
import net.pingfang.network.tcp.TcpMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TCP服务
 *
 * @author zhouhao
 * @version 1.0
 **/
public interface TcpServer extends Network {

	/**
	 * 向客户端发送数据
	 *
	 * @param message 数据对象
	 * @return 发送结果
	 */
	Mono<Boolean> send(String ip, TcpMessage message);

	Flux<NetworkMessage> subscribe();

	/**
	 * 关闭服务端
	 */
	@Override
	void shutdown();
}
