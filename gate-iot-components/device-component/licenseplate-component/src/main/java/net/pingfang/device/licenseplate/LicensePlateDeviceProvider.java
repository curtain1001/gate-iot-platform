package net.pingfang.device.licenseplate;

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
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.dll.lp.LicensePlateClient;
import net.pingfang.network.dll.lp.LpDllNetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 9:45
 */
@Slf4j
@Component
public class LicensePlateDeviceProvider implements DeviceProvider<LicensePlateDeviceProperties> {
	@Resource
	private NetworkManager networkManager;

	@Override
	public DeviceProduct getType() {
		return LicensePlateDeviceProduct.OCR_LICENSE_PLATE_III;
	}

	@Override
	public DeviceOperator createDevice(LicensePlateDeviceProperties properties) {
		LicensePlateDevice device = new LicensePlateDevice(properties.getLaneId(), properties.getId(),
				properties.getName(), networkManager);
		return init(device, properties);
	}

	public LicensePlateDevice init(LicensePlateDevice lpDevice, LicensePlateDeviceProperties properties) {
		Map<String, Object> lpProperties = JsonUtils.toObject(JsonUtils.toJsonString(properties), Map.class);
		LicensePlateClient lpClient = (LicensePlateClient) networkManager.getNetwork(LpDllNetworkType.LP_DLL,
				properties.getId());
		lpDevice.setLpClient(lpClient);
		return lpDevice;
	}

	@Override
	public void reload(DeviceOperator operator, LicensePlateDeviceProperties properties) {
		LicensePlateDevice device = (LicensePlateDevice) operator;
		init(device, properties);
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
