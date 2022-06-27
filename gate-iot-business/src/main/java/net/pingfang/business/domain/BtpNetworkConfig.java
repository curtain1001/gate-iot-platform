package net.pingfang.business.domain;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.network.NetworkConfigState;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:43
 */
@EqualsAndHashCode(callSuper = true)
@TableName("network_config")
@Data
@SuperBuilder
public class BtpNetworkConfig extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2425794209203640447L;
	private String id;

	private String name;

	private boolean enabled;

	@NotNull(message = "类型不能为空")
	private String type;

	private String description;

	private NetworkConfigState state;

	private Map<String, Object> configuration;

	public NetworkType lookupNetworkType() {
		return NetworkType.lookup(type).orElseGet(() -> NetworkType.of(type));
	}

	public NetworkProperties toNetworkProperties() {
		NetworkProperties properties = new NetworkProperties();
		properties.setConfigurations(configuration);
		properties.setEnabled(state == NetworkConfigState.enabled);
		properties.setId(getId());
		properties.setName(name);

		return properties;
	}
}
