package net.pingfang.iot.common.product;

import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description 设备产品类型
 * @date 2022-06-30 21:55
 */
public interface Product {
	/**
	 * 设备类型 （唯一）
	 *
	 * @return
	 */
	String getName();

	String getValue();

	default ObjectType getType() {
		return ObjectType.device;
	}

	default boolean isSupported(String value) {
		return getValue().equals(value);
	};
}
