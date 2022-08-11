package net.pingfang.device.licenseplate;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.device.licenseplate.config.SdkInit;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 9:45
 */
@Slf4j
@Component
public class LicensePlateDeviceProvider implements DeviceProvider<LicensePlateDeviceProperties> {
	@Override
	public Product getType() {
		return LicensePlateProduct.OCR_License_Plate;
	}

	@Override
	public DeviceOperator createDevice(LicensePlateDeviceProperties properties) {
		return init(properties);
	}

	public LicensePlateDevice init(LicensePlateDeviceProperties properties) {
		LicensePlateDevice device = new LicensePlateDevice(properties.getLaneId(), properties.getId(),
				properties.getName(), SdkInit.net);
		device.init(properties.getHost(), (short) properties.getPort(), (short) properties.getTimeout());
		return device;
	}

	@Override
	public void reload(DeviceOperator operator, LicensePlateDeviceProperties properties) {
		LicensePlateDevice device = (LicensePlateDevice) operator;
		device.reload(properties.getHost(), (short) properties.getPort(), (short) properties.getTimeout());
	}

	@Override
	public LicensePlateDeviceProperties createConfig(DeviceProperties properties) {
		LicensePlateDeviceProperties config = new LicensePlateDeviceProperties();
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
