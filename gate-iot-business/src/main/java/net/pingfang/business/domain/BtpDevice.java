package net.pingfang.business.domain;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.NetworkProperties;

/**
 * @author 王超
 * @description 设备实体
 * @date 2022-06-30 22:35
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
@TableName(value = "btp_device", autoResultMap = true)
@Accessors(chain = true)
public class BtpDevice extends BaseEntity {
	private static final long serialVersionUID = 3780581487620691310L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 设备id（采用String：设备id一般有特定命名规则）
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
	 * 开启 键值(0=开启,1=关闭)
	 */
	private int enabled;
	/**
	 * 状态 (0:启动 1：关闭)
	 */
	private int status;
	/**
	 * 设备产品类型
	 */
	@NotNull(message = "设备产品类型不能为空")
	private String product;
	/**
	 * 其他配置
	 */
	@TableField(value = "configuration", typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration;

	private transient List<Instruction> instructions;

	public DeviceProperties toProperties() {
		DeviceProperties properties = new DeviceProperties();
		properties.setDeviceId(this.deviceId);
		properties.setDeviceName(this.deviceName);
		properties.setProduct(ProductSupports.getSupport(this.product));
		properties.setLaneId(this.laneId);
		properties.setEnabled(this.enabled == 0);
		properties.setConfiguration(this.configuration);
		return properties;
	}

	public NetworkProperties toNetworkProperties() {
		NetworkProperties properties = new NetworkProperties();
		properties.setConfigurations(this.configuration);
		properties.setEnabled(this.enabled == 0);
		properties.setId(this.deviceId);
		properties.setName(this.deviceName);
		return properties;
	}

}
