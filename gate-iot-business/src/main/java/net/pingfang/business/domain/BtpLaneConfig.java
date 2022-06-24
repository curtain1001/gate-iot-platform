package net.pingfang.business.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-22 15:54
 */
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_lane_config")
public class BtpLaneConfig extends BaseEntity {
	private static final long serialVersionUID = 126557987092054351L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long laneConfigId;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 配置参数键名
	 */
	private String laneConfigKey;
	/**
	 * 配置参数值
	 */
	private String laneConfigValue;
	/**
	 * 状态 "0=正常,1=停用"
	 */
	private String status;

	@NotNull(message = "车道id不能为空")
	public Long getLaneId() {
		return laneId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	@NotBlank(message = "配置键名不能为空")
	public String getLaneConfigKey() {
		return laneConfigKey;
	}

	public void setLaneConfigKey(String laneConfigKey) {
		this.laneConfigKey = laneConfigKey;
	}
}
