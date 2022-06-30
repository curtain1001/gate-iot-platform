package net.pingfang.device.core;

import java.util.HashMap;

import com.google.common.collect.Maps;

/**
 * @author 王超
 * @description 设备支持
 * @date 2022-06-30 22:00
 */
public class ProductSupports {
	final static HashMap<Product, DeviceOperator> supports = Maps.newHashMap();

	public void registry(Product product, DeviceOperator operator) {
		supports.put(product, operator);
	}
}
