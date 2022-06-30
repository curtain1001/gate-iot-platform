package net.pingfang.device.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zhouhao
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo implements Serializable {
	private static final long serialVersionUID = -6849794470754667710L;

	/**
	 * 设备ID
	 */
	private String id;

	/**
	 * 产品ID
	 */
	private String productId;

	/**
	 * 产品版本
	 */
	private String productVersion;


	/**
	 * 其他配置
	 */
	private Map<String, Object> configuration = new HashMap<>();

	public DeviceInfo addConfig(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.put(key, value);
		return this;
	}

	public DeviceInfo addConfigIfAbsent(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.putIfAbsent(key, value);
		return this;
	}

	public DeviceInfo addConfigs(Map<String, ?> configs) {
		if (configs == null) {
			return this;
		}
		configs.forEach(this::addConfig);
		return this;
	}

}
