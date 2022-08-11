package net.pingfang.device.plc;

import java.lang.reflect.InvocationTargetException;
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
import net.pingfang.network.NetworkManager;

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
		PLCDevice device = new PLCDevice(properties.getLaneId(), properties.getId(), properties.getName(),
				networkManager);
		return init(device, properties);
	}

	public PLCDevice init(PLCDevice plcDevice, PLCDeviceProperties properties) {
		Map<String, Object> tcpProperties = JsonUtils.toObject(JsonUtils.toJsonString(properties), Map.class);
		plcDevice.setTcpClient(tcpProperties);
		return plcDevice;
	}

	@Override
	public void reload(DeviceOperator operator, PLCDeviceProperties properties) {
		init((PLCDevice) operator, properties);
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
