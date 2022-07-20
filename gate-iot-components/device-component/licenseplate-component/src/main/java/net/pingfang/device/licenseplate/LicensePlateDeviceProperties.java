package net.pingfang.device.licenseplate;

import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 9:42
 */
@Data
public class LicensePlateDeviceProperties {
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
	 * 超时时间
	 */
	private long timeout;
	/**
	 * 是否自动开启
	 */
	private boolean enabled;
}
