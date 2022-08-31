package net.pingfang.business.domain;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.business.enums.NetworkConfigState;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;
import net.pingfang.network.Control;
import net.pingfang.network.NetworkProperties;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:43
 */
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_network_config", autoResultMap = true)
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

	@TableField(jdbcType = JdbcType.VARCHAR, javaType = true, typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration;
	/**
	 * 网络组件对象类型 设备与服务
	 */
	private Control control;

	public NetworkType lookupNetworkType() {
		return NetworkType.lookup(type).orElseGet(() -> NetworkType.of(type));
	}

	public NetworkProperties toNetworkProperties() {
		NetworkProperties properties = new NetworkProperties();
		properties.setConfigurations(configuration);
		properties.setEnabled(status == NetworkConfigState.enabled);
		properties.setId(networkConfigId);
		properties.setName(name);
		properties.setControl(control);
		properties.setNetworkType(NetworkTypes.lookup(type).get());
		return properties;
	}
}
