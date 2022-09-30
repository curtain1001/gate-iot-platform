package net.pingfang.network.dll.lp;

import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.network.Network;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-26 9:19
 */
public interface LpClient extends Network {

	int getHandle();

	/**
	 * 订阅消息
	 */
	Flux<NetworkMessage> subscribe();

	/**
	 * 关闭服务端
	 */
	@Override
	void shutdown();
}
