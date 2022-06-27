package net.pingfang.device.testconfig;

import org.springframework.stereotype.Component;

import net.pingfang.network.NetworkConfigManager;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkType;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 18:10
 */
@Component
public class DefaultNetworkConfigManager implements NetworkConfigManager {
	@Override
	public Mono<NetworkProperties> getConfig(NetworkType networkType, String id) {
		return null;
	}
}
