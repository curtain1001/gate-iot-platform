package net.pingfang.servicecomponent.core;

import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-14 16:27
 */
public interface ServerOperator {
	/**
	 * 服务号
	 *
	 * @return 服务号
	 */
	String getServerId();

	/**
	 * 服务名称
	 *
	 * @return 服务名称
	 */

	String getServerName();

	/**
	 * 车道id
	 */
	Long getLaneId();

	/**
	 * 设备产品类型
	 *
	 * @return 类型实例
	 */
	Product getProduct();

	/**
	 *
	 * 关闭
	 */
	void close();

	/**
	 * 订阅消息
	 */
	Flux<FunctionMessage> subscribe(Long laneId);

	/**
	 * 是否存活
	 *
	 * @return 是否存活
	 */
	boolean isAlive();

	/**
	 * 是否自动启动
	 *
	 * @return 是否自动启动
	 */

	boolean isAutoReload();

}
