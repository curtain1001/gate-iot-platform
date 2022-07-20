package net.pingfang.device.licenseplate;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;

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

	@Component
	private static class LPSupportRegistry {
		@PostConstruct
		public void init() {
			ProductSupports.registry(OCR_License_Plate);
		}
	}
}
