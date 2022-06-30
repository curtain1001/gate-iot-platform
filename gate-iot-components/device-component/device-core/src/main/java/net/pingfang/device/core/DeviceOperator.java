package net.pingfang.device.core;

import java.util.HashMap;

import net.pingfang.iot.common.EncodedMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	Long getDeviceId();

	/**
	 * 车道主键
	 *
	 * @return 车道主键
	 */
	String getLaneId();

	/**
	 * 设备代码
	 *
	 * @return 设备代码
	 */
	String getDeviceCode();

	/**
	 * 设备名称
	 *
	 * @return 设备名称
	 */
	String getDeviceName();

	/**
	 * 获取状态
	 *
	 * @return 状态
	 */
	String getStatus();

	/**
	 *
	 * 断开连接
	 */
	void disconnect();

	/**
	 * 订阅TCP消息,此消息是已经处理过粘拆包的完整消息
	 *
	 * @return TCP消息
	 */
	Flux<EncodedMessage> subscribe();

	/**
	 * 向客户端发送数据
	 *
	 * @param message 数据对象
	 * @return 发送结果
	 */
	Mono<Boolean> send(EncodedMessage message);

	/**
	 * 其他配置
	 */
	HashMap<String, Object> getProperties();

}
