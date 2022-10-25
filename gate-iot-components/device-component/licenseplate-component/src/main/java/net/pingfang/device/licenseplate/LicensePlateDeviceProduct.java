package net.pingfang.device.licenseplate;

import java.util.List;

import com.google.common.collect.Lists;

import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.iot.common.product.DeviceProductSupports;
import net.pingfang.network.dll.lp.LpDllNetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:57
 */
public enum LicensePlateDeviceProduct implements DeviceProduct {
	OCR_LICENSE_PLATE_III("第三代车牌识别相机");

	static {
		DeviceProductSupports.register(OCR_LICENSE_PLATE_III);
	}

	public final String name;

	LicensePlateDeviceProduct(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return name();
	}

	@Override
	public List<NetworkType> getNetwork() {
		return Lists.newArrayList(LpDllNetworkType.LP_DLL);
	}

}
