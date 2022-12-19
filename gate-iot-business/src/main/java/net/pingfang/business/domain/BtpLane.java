package net.pingfang.business.domain;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.business.values.LabelObject;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description 车道信息
 * @date 2022-06-21 10:52
 */
@SuperBuilder
@TableName(value = "btp_lane")
@AllArgsConstructor
@NoArgsConstructor
public class BtpLane extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5792869601575018184L;
	/**
	 * 车道id
	 */
	@TableId(type = IdType.AUTO)
	private Long laneId;
	/**
	 * 车道名称
	 */
	private String laneName;

	/**
	 * 车道编号
	 */
	private String laneNo;

	/**
	 * 场站号
	 */
	private Long areaId;

	/**
	 * 海关通道号
	 */
	private String customsLaneNo;

	/**
	 * 通道ip
	 */
	private String ip;

	/**
	 * 进出类型（0:进；1：出）
	 */
	private String type;

	public LabelObject toLabelObject() {
		return LabelObject.builder() //
				.label(laneName) //
				.value(laneId)//
				.build(); //
	}

	public Long getLaneId() {
		return laneId;
	}

	public void setLaneId(Long laneId) {
		this.laneId = laneId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getAreaId() {
		return areaId;
	}

	@NotBlank(message = "通道名称不能为空")
	@Size(min = 0, max = 100, message = "参数名称不能超过100个字符")
	public String getLaneName() {
		return laneName;
	}

	public void setLaneName(String laneName) {
		this.laneName = laneName;
	}

	@NotBlank(message = "通道编号不能为空")
	@Size(min = 0, max = 10, message = "参数名称不能超过10个字符")
	public String getLaneNo() {
		return laneNo;
	}

	public void setLaneNo(String laneNo) {
		this.laneNo = laneNo;
	}

	public String getCustomsLaneNo() {
		return customsLaneNo;
	}

	public void setCustomsLaneNo(String customsLaneNo) {
		this.customsLaneNo = customsLaneNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@NotBlank(message = "IP地址不能为空")
	@Size(min = 0, max = 24, message = "参数名称不能超过24个字符")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
