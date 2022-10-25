package net.pingfang.device.plc;

import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-11 17:38
 */
@Data
public class PLCDeviceProperties {
	/**
	 * 设备id
	 */
	private String id;
	/**
	 * 设备名称
	 */
	private String name;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * ip
	 */
	private String host;
	/**
	 * 证书id
	 */

	private String certId;
	/**
	 * 是否开启ssl
	 */

	private boolean ssl;
	/**
	 * 是否保活（长连接）
	 */
	private boolean keepalive;
	/**
	 * 是否自动开启
	 */
	private boolean enabled;
}
