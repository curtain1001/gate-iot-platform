package net.pingfang.device.plc;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:33
 */
public enum PLCProduct implements Product {
	PLC("PLC");

	private final String name;

	PLCProduct(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return this.name();
	}

	@Component
	private static class PLCSupportRegistry {
		@PostConstruct
		public void init() {
			ProductSupports.registry(PLC);
		}
	}

}
