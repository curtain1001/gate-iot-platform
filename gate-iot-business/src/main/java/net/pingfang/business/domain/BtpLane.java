package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description 车道信息
 * @date 2022-06-21 10:52
 */
@SuperBuilder
@TableName(value = "btp_lane")
public class BtpLane extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5792869601575018184L;
	/**
	 * 车道id
	 */
	@TableId(type = IdType.AUTO)
	private String laneId;
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
	private String areaNo;

	/**
	 * 海关通道号
	 */
	private String customsLaneNo;

	/**
	 * 进出类型
	 */
	private String type;

	public String getLaneId() {
		return laneId;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
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

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getCustomsLaneNo() {
		return customsLaneNo;
	}

	public void setCustomsLaneNo(String customsLaneNo) {
		this.customsLaneNo = customsLaneNo;
	}
}
