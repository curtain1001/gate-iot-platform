package net.pingfang.iot.common;

import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-26 15:14
 */
public interface NetworkSession {
	Flux<NetworkMessage> subscribe();

	void shutdown();

	/**
	 * @return 是否存活
	 */
	boolean isAlive();
}
