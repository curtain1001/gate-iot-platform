package net.pingfang.business.domain;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 服务模块
 * </p>
 *
 * @author 王超
 * @since 2022-12-02 15:32
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_module", autoResultMap = true)
public class BtpModule extends BaseEntity {
	private static final long serialVersionUID = -781372519309732797L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 模块名
	 */
	private String moduleName;
	/**
	 * 模块代码
	 */
	private String moduleCode;
	/**
	 * 车道id
	 */
	private long laneId;
	/**
	 * 设备列表
	 */
	@TableField(value = "device_ids", typeHandler = JacksonTypeHandler.class)
	private List<String> deviceIds;

	private transient String deviceId;

	private transient List<BtpDevice> deviceList;

	/**
	 * 开关
	 */
	private boolean enabled;

	/**
	 * 其他配置
	 */
	@TableField(value = "configuration", typeHandler = JacksonTypeHandler.class)
	private Map<String, Object> configuration;

}
