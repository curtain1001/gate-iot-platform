package net.pingfang.business.domain;

import java.util.Map;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 系统支撑服务
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 9:40
 */
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_support_server", autoResultMap = true)
public class BtpSupportServer extends BaseEntity {
	private static final long serialVersionUID = 695479576251012469L;
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 产品类型
	 */
	private String product;
	/**
	 * 网络组件类型
	 */
	private String networkType;
	/**
	 * 网络组件id
	 */
	private String networkId;
	/**
	 * 网络组件名称
	 */
	private transient String networkName;
	/**
	 * 配置信息
	 */
	@TableField(jdbcType = JdbcType.VARCHAR, javaType = true, typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration;

	public Map<String, Object> getConfiguration() {
		if (configuration == null) {
			configuration = Maps.newHashMap();
		}
		configuration.put("networkId", this.networkId);
		return configuration;
	}
}
