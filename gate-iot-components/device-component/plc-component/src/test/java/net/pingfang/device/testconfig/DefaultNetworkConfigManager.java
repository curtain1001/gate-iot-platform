package net.pingfang.device.testconfig;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.NetworkConfigManager;
import net.pingfang.network.NetworkProperties;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 18:10
 */
@Component
public class DefaultNetworkConfigManager implements NetworkConfigManager {
	@Override
	public NetworkProperties getConfig(NetworkType networkType, String id) {
		NetworkProperties properties = new NetworkProperties();
		HashMap<String, Object> hashMap = Maps.newHashMap();
		hashMap.put("host", "192.168.1.150");
		hashMap.put("port", "2000");
		hashMap.put("parserType", "fixed_length");

		HashMap<String, Object> parserConfiguration = Maps.newHashMap();
		parserConfiguration.put("size", "4");
		hashMap.put("parserConfiguration", parserConfiguration);
		properties.setConfigurations(hashMap);
		properties.setEnabled(true);
		properties.setId("TCP_CLIENT::001");
		properties.setName("customs");
		return properties;
	}

	@Override
	public NetworkProperties getConfig(String configId) {
		return null;
	}

	@Override
	public List<NetworkProperties> getConfig() {
		return null;
	}

	@Override
	public void update(String configId, String state) {

	}
}
