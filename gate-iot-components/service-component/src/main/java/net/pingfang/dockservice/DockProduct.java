package net.pingfang.dockservice;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-22 16:43
 */
public enum DockProduct implements Product {
	DOCK_SERVICE;

	@Override
	public String getName() {
		return "对接服务";
	}

	@Override
	public String getValue() {
		return "DOCK_SERVICE";
	}

	@Override
	public ObjectType getType() {
		return ObjectType.service;
	}

	@Component
	private static class DockProductRegistry {
		@PostConstruct
		public void init() {
			ProductSupports.registry(DOCK_SERVICE);
		}
	}

}
