package net.pingfang.device.novaled;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.nova.NovaLed;

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
	@Resource
	NetworkManager networkManager;

	@Override
	public DeviceProduct getType() {
		return NovaLedDeviceProduct.NOVA_LED;
	}

	@Override
	public DeviceOperator createDevice(NovaLedDeviceProperties properties) {
		NovaLed novaLed = (NovaLed) networkManager.getNetwork(properties.getNetworkType(), properties.getId());
		NovaLedDevice novaLedDevice = new NovaLedDevice(properties.getLaneId(), properties.getId(),
				properties.getName(),
				properties);
		novaLedDevice.setNovaLed(novaLed);
		return novaLedDevice;
	}

	@Override
	public void reload(DeviceOperator deviceOperator, NovaLedDeviceProperties properties) {
		NovaLedDevice device = ((NovaLedDevice) deviceOperator);
		NovaLed novaLed = (NovaLed) networkManager.getNetwork(properties.getNetworkType(), properties.getId());
		device.setProperties(properties);
		device.setNovaLed(novaLed);
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
		config.setNetworkType(properties.getNetworkType());
		return config;
	}
}
