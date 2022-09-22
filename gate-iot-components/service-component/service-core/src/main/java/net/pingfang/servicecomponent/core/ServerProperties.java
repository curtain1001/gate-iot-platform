package net.pingfang.servicecomponent.core;

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
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 15:43
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerProperties implements Serializable {
	private static final long serialVersionUID = 4183440779092210858L;
	/**
	 * 设备id
	 */
	private String serverId;
	/**
	 * 设备名称
	 */
	private String serverName;
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

	private Map<String, Object> configuration = new HashMap<>();

	public ServerProperties addConfig(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.put(key, value);
		return this;
	}

	public ServerProperties addConfigIfAbsent(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.putIfAbsent(key, value);
		return this;
	}

	public ServerProperties addConfigs(Map<String, ?> configs) {
		if (configs == null) {
			return this;
		}
		configs.forEach(this::addConfig);
		return this;
	}
}
