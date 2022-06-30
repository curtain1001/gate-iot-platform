package net.pingfang.device.core;

import net.pingfang.network.NetworkManager;

/**
 * @author 王超
 * @description 设备构造
 * @date 2022-06-30 23:57
 */
public interface DeviceOperatorBuilderStrategy {
	Product getProduct();

	DeviceOperator build(DeviceInfo info, NetworkManager networkManager);
}
