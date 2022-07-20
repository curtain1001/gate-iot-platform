package net.pingfang.business.domain;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.business.enums.NetworkConfigState;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@TableName("btp_network_config")
public class BtpNetworkConfig extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -2425794209203640447L;
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 存在设备的情况 为设备id
	 */
	private String networkConfigId;

	private String name;

	private boolean enabled;

	@NotNull(message = "类型不能为空")
	private String type;

	private NetworkConfigState status;

	private Map<String, Object> configuration;

	public NetworkType lookupNetworkType() {
		return NetworkType.lookup(type).orElseGet(() -> NetworkType.of(type));
	}

	public NetworkProperties toNetworkProperties() {
		NetworkProperties properties = new NetworkProperties();
		properties.setConfigurations(configuration);
		properties.setEnabled(status == NetworkConfigState.enabled);
		properties.setId(networkConfigId);
		properties.setName(name);

		return properties;
	}
}
