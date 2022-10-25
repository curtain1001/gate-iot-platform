package net.pingfang.business.domain;

import java.util.List;
import java.util.Map;

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
import net.pingfang.business.values.KeyValuePair;
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
@TableName(value = "btp_support_service", autoResultMap = true)
public class BtpSupportService extends BaseEntity {
	private static final long serialVersionUID = 695479576251012469L;
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 服务名称
	 */
	private String serviceName;
	/**
	 * 服务代码
	 */
	private String serviceCode;
	/**
	 * 设备id
	 */
	private transient List<Long> deviceIds;
	/**
	 * 开关
	 */
	private boolean enabled;
	/**
	 * 设备名称 key:设备id；value:设备名称
	 */
	private transient List<KeyValuePair> devices;

	/**
	 * 配置信息
	 */
	@TableField(jdbcType = JdbcType.VARCHAR, javaType = true, typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration;

}
