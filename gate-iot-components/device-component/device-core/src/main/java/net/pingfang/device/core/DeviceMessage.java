package net.pingfang.device.core;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-08 16:11
 */
public class DeviceMessage {
	/**
	 * 设备id
	 */
	private String deviceId;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 消息内容类型
	 */
	private MessageType type;

	private Product product;

	private JsonNode jsonNode;
}
