package net.pingfang.device.core.manager;

import java.util.Map;

import com.google.common.collect.Maps;

import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:54
 */
public class DefaultLaneDeviceManager {
	Map<Long, Map<DeviceProduct, DeviceOperator>> laneDevice = Maps.newHashMap();

	public void register() {

	}
}
