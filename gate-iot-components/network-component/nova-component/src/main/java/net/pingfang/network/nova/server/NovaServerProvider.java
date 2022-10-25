package net.pingfang.network.nova.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;

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
public class NovaServerProvider implements NetworkProvider<NovaServerProperties> {
	@Override
	public NetworkType getType() {
		return NovaServerNetworkType.NOVA_SERVER;
	}

	@Override
	public NovaServer createNetwork(NovaServerProperties properties) {
		try {
			ServerSocket serverSocket = new ServerSocket(properties.getPort());
			NovaTraffic novaTraffic = new NovaTraffic(serverSocket.accept());
			DefaultProperties defaultProperties = DefaultProperties.builder() //
					.displayHigh(properties.getDisplayHigh())//
					.displayWidth(properties.getDisplayWidth())//
					.dwellTime(properties.getDwellTime())//
					.build();
			return new NovaServer(properties.getId(), properties.getLaneId(), novaTraffic, defaultProperties);
		} catch (IOException e) {
			log.error("novaLED服务器创建失败：", e);
			throw new RuntimeException("NOVA_LED服务器创建失败");
		}
	}

	@Override
	public void reload(Network network, NovaServerProperties properties) {
		try {
			ServerSocket serverSocket = new ServerSocket(properties.getPort());
			NovaTraffic novaTraffic = new NovaTraffic(serverSocket.accept());
			DefaultProperties defaultProperties = DefaultProperties.builder() //
					.displayHigh(properties.getDisplayHigh())//
					.displayWidth(properties.getDisplayWidth())//
					.dwellTime(properties.getDwellTime())//
					.build();
			((NovaServer) network).setNovaTraffic(novaTraffic);
			((NovaServer) network).setDefaultProperties(defaultProperties);
		} catch (IOException e) {
			log.error("novaLED服务器重启失败：", e);
			throw new RuntimeException("NOVA_LED服务器重启失败");
		}
	}

	@Override
	public NovaServerProperties createConfig(NetworkProperties properties)
			throws InvocationTargetException, IllegalAccessException {
		NovaServerProperties config = new NovaServerProperties();
		BeanUtils.copyBean(config, properties.getConfigurations());
		config.setId(properties.getId());
		config.setLaneId(properties.getLaneId());
		return config;
	}

}
