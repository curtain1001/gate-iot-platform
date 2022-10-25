package net.pingfang.device.novaled;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pingfang.iot.common.network.NetworkType;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 15:58
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NovaLedDeviceProperties {
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
	 * ip
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;

	private NetworkType networkType;
	/**
	 * 显示设置
	 */
	private Map<String, Object> properties;
}
