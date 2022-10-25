package net.pingfang.network.nova.client;

import java.lang.reflect.InvocationTargetException;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.bean.BeanUtils;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.Network;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkProvider;
import net.pingfang.network.nova.DefaultProperties;
import nova.traffic.NovaTraffic;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 19:56
 */
@Component
@Slf4j
public class NovaClientProvider implements NetworkProvider<NovaClientProperties> {
	@Override
	public NetworkType getType() {
		return NovaClientNetworkType.NOVA_CLIENT;
	}

	@Override
	public NovaClient createNetwork(NovaClientProperties properties) {
		NovaTraffic novaTraffic = new NovaTraffic(properties.getIp(), properties.getPort());
		DefaultProperties defaultProperties = DefaultProperties.builder() //
				.displayHigh(properties.getDisplayHigh())//
				.displayWidth(properties.getDisplayWidth())//
				.dwellTime(properties.getDwellTime())//
				.build();
		return new NovaClient(properties.getIp(), properties.getLaneId(), novaTraffic, defaultProperties);
	}

	@Override
	public void reload(Network network, NovaClientProperties properties) {
		NovaTraffic novaTraffic = new NovaTraffic(properties.getIp(), properties.getPort());
		DefaultProperties defaultProperties = DefaultProperties.builder() //
				.displayHigh(properties.getDisplayHigh())//
				.displayWidth(properties.getDisplayWidth())//
				.dwellTime(properties.getDwellTime())//
				.build();
		((NovaClient) network).setNovaTraffic(novaTraffic);
		((NovaClient) network).setDefaultProperties(defaultProperties);
		((NovaClient) network).init();
	}

	@Override
	public NovaClientProperties createConfig(NetworkProperties properties)
			throws InvocationTargetException, IllegalAccessException {
		NovaClientProperties config = new NovaClientProperties();
		BeanUtils.copyBean(config, properties.getConfigurations());
		config.setIp(properties.getId());
		config.setLaneId(properties.getLaneId());
		return config;
	}

}
