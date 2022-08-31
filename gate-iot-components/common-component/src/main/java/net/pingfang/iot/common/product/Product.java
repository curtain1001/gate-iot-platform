package net.pingfang.iot.common.product;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.network.NetworkType;

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

	default NetworkType getNetwork() {
		return null;
	}

	/**
	 * 默认配置
	 */
	default Map<String, Object> getDefaultProperties() {
		return Maps.newHashMap();
	}

	default List<CustomizedSettingData> getBasicForm() {
		return Lists.newArrayList();
	};

	default ObjectType getType() {
		return ObjectType.device;
	}

	default boolean isSupported(String value) {
		return getValue().equals(value);
	};

}
