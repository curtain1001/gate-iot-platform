package net.pingfang.device.novaled;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.device.novaled.core.NovaLedDevice;
import net.pingfang.iot.common.product.Product;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 15:58
 */
@Slf4j
@Component
public class NovaLedDeviceProvider implements DeviceProvider<NovaLedDeviceProperties> {

	@Override
	public Product getType() {
		return NovaLedDeviceProduct.NOVA_LED;
	}

//	public NovaLedDevice init(NovaLedDevice novaLedDevice, NovaLedDeviceProperties properties) {
//		Map<String, Object> tcpProperties = JsonUtils.toObject(JsonUtils.toJsonString(properties), Map.class);
//		novaLedDevice.setTcpClient(tcpProperties);
//		return novaLedDevice;
//	}

	@Override
	public DeviceOperator createDevice(NovaLedDeviceProperties properties) {
		NovaLedDevice device = new NovaLedDevice(properties.getLaneId(), properties.getId(), properties.getName(),
				properties);
		return device;
	}

	@Override
	public void reload(DeviceOperator deviceOperator, NovaLedDeviceProperties novaLedDeviceProperties) {
		((NovaLedDevice) deviceOperator).setProperties(novaLedDeviceProperties);
	}

	@Override
	public NovaLedDeviceProperties createConfig(DeviceProperties properties) {
		NovaLedDeviceProperties config = new NovaLedDeviceProperties();
		try {
			BeanUtils.populate(config, properties.getConfiguration());
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("诺瓦LED设备配置拷贝失败：", e);
		}
		config.setId(properties.getDeviceId());
		config.setLaneId(properties.getLaneId());
		config.setName(properties.getDeviceName());
		return config;
	}
}
