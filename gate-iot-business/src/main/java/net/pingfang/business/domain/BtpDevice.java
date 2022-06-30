package net.pingfang.business.domain;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.device.core.Product;

/**
 * @author 王超
 * @description 设备实体
 * @date 2022-06-30 22:35
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
@TableName("btp_device")
public class BtpDevice extends BaseEntity {
	/**
	 * 设备id
	 */
	@TableId(type = IdType.AUTO)
	private Long deviceId;
	/**
	 * 设备名称
	 */
	private String deviceName;
	/**
	 * 车道id
	 */
	private String laneId;
	/**
	 * 设备产品类型
	 */
	private Product product;
	/**
	 * 其他配置
	 */
	@TableField(value = "configuration", typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration = new HashMap<>();

	public BtpDevice addConfig(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.put(key, value);
		return this;
	}

	public BtpDevice addConfigIfAbsent(String key, Object value) {
		if (configuration == null) {
			configuration = new HashMap<>();
		}
		configuration.putIfAbsent(key, value);
		return this;
	}

	public BtpDevice addConfigs(Map<String, ?> configs) {
		if (configs == null) {
			return this;
		}
		configs.forEach(this::addConfig);
		return this;
	}

}
