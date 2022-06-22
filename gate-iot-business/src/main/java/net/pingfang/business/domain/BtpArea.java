package net.pingfang.business.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 11:00
 */

@SuperBuilder(toBuilder = true)
@TableName(value = "btp_area")
@AllArgsConstructor
@NoArgsConstructor
public class BtpArea extends BaseEntity implements Serializable {
	@TableField(exist = false)
	public static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long areaId;
	/**
	 * 场站名
	 */
	@TableField(value = "area_name", condition = SqlCondition.LIKE)
	private String areaName;
	/**
	 * 场站号
	 */
	@TableField(value = "area_no", condition = SqlCondition.LIKE)
	private String areaNo;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@NotBlank(message = "场站名称不能为空")
	@Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@NotBlank(message = "场站号不能为空")
	@Size(min = 0, max = 10, message = "参数名称不能超过10个字符")
	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

}
