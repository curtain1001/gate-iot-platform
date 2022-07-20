package net.pingfang.device.core;

import java.util.List;

import net.pingfang.iot.common.product.Product;

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
	 * @param id  网络组件id
	 * @param <T> NetWork子类泛型
	 * @return 网络组件
	 */
	<T extends DeviceOperator> DeviceOperator getDevice(Product type, String id);

	/**
	 * 创建设备
	 *
	 * @param id  网络组件id
	 * @param <T> NetWork子类泛型
	 * @return 网络组件
	 */
	<T extends DeviceOperator> DeviceOperator create(Product type, String id);

	/**
	 * 获取所有的网络组件支持提供商
	 *
	 * @return 网络组件支持提供商
	 */
	List<DeviceProvider<?>> getProviders();

	/**
	 * 重新加载网络组件
	 *
	 * @param type 网络类型
	 * @param id   网络组件ID
	 * @return void
	 */
	void reload(Product type, String id);

	/**
	 * 停止网络组件
	 *
	 * @param type 网络类型
	 * @param id   网络组件ID
	 * @return void
	 */
	void shutdown(Product type, String id);
}
