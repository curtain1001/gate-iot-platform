package net.pingfang.device.licenseplate;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.DefaultNetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:57
 */
public enum LicensePlateProduct implements Product {
	OCR_License_Plate("第三代车牌识别相机");

	public final String name;

	LicensePlateProduct(String name) {
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
	public NetworkType getNetwork() {
		return DefaultNetworkType.LP_DLL;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(LpBasicFormCustomized.values());
	}

	@Component
	private static class LPSupportRegistry {
		@PostConstruct
		public void init() {
			ProductSupports.registry(OCR_License_Plate);
		}
	}
}
