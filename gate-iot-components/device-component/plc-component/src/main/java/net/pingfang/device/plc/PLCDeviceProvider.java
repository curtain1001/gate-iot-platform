package net.pingfang.device.plc;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.parser.PayloadParserType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-11 17:38
 */
@Slf4j
@Component
public class PLCDeviceProvider implements DeviceProvider<PLCDeviceProperties> {
	@Resource
	private NetworkManager networkManager;

	@Override
	public Product getType() {
		return PLCProduct.PLC;
	}

	@Override
	public DeviceOperator createDevice(PLCDeviceProperties properties) {
		PLCDevice plcDevice = init(properties);
		plcDevice.subscribe();
		return plcDevice;
	}

	public PLCDevice init(PLCDeviceProperties properties) {
		Map tcpProperties = JsonUtils.toObject(JsonUtils.toJsonString(properties), Map.class);
		// 粘黏包处理设置
		tcpProperties.put("parserType", PayloadParserType.FIXED_LENGTH);
		tcpProperties.put("parserConfiguration", Collections.singletonMap("size", 4));

		NetworkProperties networkProperties = new NetworkProperties();
		networkProperties.setId(properties.getId());
		networkProperties.setName("PLC::TCP::CLIENT::" + properties.getId());
		networkProperties.setEnabled(true);
		networkProperties.setConfigurations(tcpProperties);

		TcpClient tcpClient = (TcpClient) networkManager.getNetwork(DefaultNetworkType.TCP_CLIENT, networkProperties,
				properties.getId());
		return new PLCDevice(properties.getLaneId(), properties.getId(), properties.getName(), tcpClient);
	}

	@Override
	public DeviceOperator reload(DeviceOperator operator, PLCDeviceProperties properties) {
//		TcpClient tcpClient = (TcpClient) networkManager.getNetwork(DefaultNetworkType.TCP_CLIENT,
//				operator.getDeviceId());
		operator.shutdown();
		return init(properties);
	}

	@Override
	public PLCDeviceProperties createConfig(DeviceProperties properties) {
		PLCDeviceProperties config = new PLCDeviceProperties();
		try {
			BeanUtils.populate(config, properties.getConfiguration());
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("PLC设备配置拷贝失败：", e);
		}
		config.setId(properties.getDeviceId());
		config.setLaneId(properties.getLaneId());
		config.setName(properties.getDeviceName());
		return config;
	}

}
