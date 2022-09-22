package net.pingfang.device.core;

import java.util.List;

import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * 网络服务管理器
 * <p>
 * 管理所有的网络组件
 *
 * @author zhouhao
 * @since 1.0
 */
public interface DeviceManager {

	/**
	 * 根据ID获取设备
	 *
	 * @param laneId   车道id
	 * @param deviceId 设备id
	 * @return 设备
	 */
	<T extends DeviceOperator> DeviceOperator getDevice(Long laneId, String deviceId);

	/**
	 * 根据ID获取设备
	 *
	 * @param product 设备类型
	 * @return 设备
	 */
	<T extends DeviceOperator> List<DeviceOperator> getDevices(Product product);

	/**
	 * 创建设备
	 *
	 * @param laneId   车道id
	 * @param deviceId 设备id
	 * @param type     设备类型
	 * @return 网络组件
	 */
	<T extends DeviceOperator> DeviceOperator create(Long laneId, String deviceId, Product type);

	/**
	 * 获取所有的网络组件支持提供商
	 *
	 * @return 网络组件支持提供商
	 */
	List<DeviceProvider<?>> getProviders();

	/**
	 * 重新加载网络组件
	 *
	 * @param laneId   车道id
	 * @param deviceId 设备id
	 * @return void
	 */
	void reload(Long laneId, String deviceId);

	/**
	 * 停止网络组件
	 *
	 * @param laneId   车道id
	 * @param deviceId 设备id
	 * @return void
	 */
	void shutdown(Long laneId, String deviceId);

	/**
	 * 订阅设备创建
	 *
	 * @return 设备
	 */
	Flux<DeviceOperator> subscribe();
}
