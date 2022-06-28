package net.pingfang.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.pingfang.business.service.IBtpNetworkConfigService;
import net.pingfang.network.NetworkConfigManager;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 11:00
 */
@Component
public class DefaultNetworkConfigManager implements NetworkConfigManager {
	@Resource
	public IBtpNetworkConfigService networkConfigService;

	@Override
	public NetworkProperties getConfig(NetworkType networkType, String id) {
		return networkConfigService.getById(id).toNetworkProperties();
	}
}
