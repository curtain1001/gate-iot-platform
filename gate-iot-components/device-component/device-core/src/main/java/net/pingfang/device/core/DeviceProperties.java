package net.pingfang.device.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-11 17:36
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProperties implements Serializable {
	private static final long serialVersionUID = -6849794470754667710L;
	/**
	 * 设备id
	 */
	private String deviceId;
	/**
	 * 设备名称
	 */
	private String deviceName;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 设备产品类型
	 */
	private Product product;
	/**
	 * 开启关闭
	 */
	private boolean enabled;

	/**
	 * 其他配置(key:network,value:NetworkProperties)
	 */
	private Map<String, Object> configuration = new HashMap<>();

	public DeviceProperties addConfig(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.put(key, value);
		return this;
	}

	public DeviceProperties addConfigIfAbsent(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.putIfAbsent(key, value);
		return this;
	}

	public DeviceProperties addConfigs(Map<String, ?> configs) {
		if (configs == null) {
			return this;
		}
		configs.forEach(this::addConfig);
		return this;
	}
}
