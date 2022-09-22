package net.pingfang.servicecomponent.core;

import java.util.List;

import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 16:12
 */
public interface ServerManager {

	/**
	 * 根据ID获取设备
	 *
	 * @param laneId   车道id
	 * @param serverId 服务id
	 * @return 设备
	 */
	<T extends ServerOperator> ServerOperator getServer(Long laneId, String serverId);

	/**
	 * 根据ID获取设备
	 *
	 * @param product 设备类型
	 * @return 设备
	 */
	<T extends ServerOperator> List<ServerOperator> getServers(Product product);

	/**
	 * 创建设备
	 *
	 * @param laneId   车道id
	 * @param serverId 设备id
	 * @param type     设备类型
	 * @return 网络组件
	 */
	<T extends ServerOperator> ServerOperator create(Long laneId, String serverId, Product type);

	/**
	 * 获取所有的网络组件支持提供商
	 *
	 * @return 网络组件支持提供商
	 */
	List<ServerProvider<?>> getProviders();

	/**
	 * 重新加载网络组件
	 *
	 * @param laneId   车道id
	 * @param serverId 服务id
	 * @return void
	 */
	void reload(Long laneId, String serverId);

	/**
	 * 停止服务
	 *
	 * @param laneId   车道id
	 * @param serverId 设备id
	 * @return void
	 */
	void shutdown(Long laneId, String serverId);

	Flux<ServerOperator> subscribe();

}
