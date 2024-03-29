package net.pingfang.servicecomponent.core;

import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;

/**
 * 设备支持提供商
 *
 * @param <P> 设备类型
 */
public interface ServerProvider<P> {

	/**
	 * @return 类型
	 * @see DefaultNetworkType
	 */
	Product getType();

	/**
	 * 使用配置创建一个网络组件
	 *
	 * @param properties 配置信息
	 * @return 网络组件
	 */
	ServerOperator createServer(P properties);

	/**
	 * 重新加载网络组件
	 *
	 * @param operator   网络组件
	 * @param properties 配置信息
	 */
	void reload(ServerOperator operator, P properties);

	/**
	 * 根据可序列化的配置信息创建网络组件配置
	 *
	 * @param properties 原始配置信息
	 * @return 网络配置信息
	 */
	P createConfig(ServerProperties properties);

}
