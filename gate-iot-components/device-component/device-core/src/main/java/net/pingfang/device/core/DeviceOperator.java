package net.pingfang.device.core;

import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 15:49
 */

public interface DeviceOperator {
	/**
	 * 设备号
	 *
	 * @return
	 */
	String getDeviceId();

	/**
	 * 车道主键
	 *
	 * @return 车道主键
	 */
	Long getLaneId();

	/**
	 * 设备名称
	 *
	 * @return 设备名称
	 */
	String getDeviceName();

	/**
	 * 设备产品类型
	 *
	 * @return 类型实例
	 */
	Product getProduct();

	/**
	 *
	 * 断开连接
	 */
	void shutdown();

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
	 * 当{@link DeviceOperator#isAlive()}为false是,是否自动重新加载.
	 *
	 * @return 是否重新加载
	 * @see DeviceProvider#reload(DeviceOperator, Object)
	 */
	boolean isAutoReload();

	/**
	 * 心跳
	 */
	void keepAlive();

}
