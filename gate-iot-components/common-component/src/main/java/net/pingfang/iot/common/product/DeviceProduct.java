package net.pingfang.iot.common.product;

import java.util.List;

import net.pingfang.iot.common.network.NetworkType;

/**
 * @author 王超
 * @description 设备产品类型
 * @date 2022-06-30 21:55
 */
public interface DeviceProduct {
	/**
	 * 设备类型 （唯一）
	 *
	 * @return
	 */
	String getName();

	String getValue();

	List<NetworkType> getNetwork();

	default boolean meets(NetworkType type) {
		return getNetwork().contains(type);
	}

}
