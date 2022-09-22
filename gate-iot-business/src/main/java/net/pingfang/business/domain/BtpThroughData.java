package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 车道通过信息
 * </p>
 *
 * @author 王超
 * @since 2022-09-21 16:11
 */
@SuperBuilder(toBuilder = true)
@TableName(value = "btp_area")
@AllArgsConstructor
@NoArgsConstructor
public class BtpThroughData extends BaseEntity {

	private static final long serialVersionUID = 3895565611410918708L;

	@TableId(type = IdType.AUTO)
	private Long dataId;

	private String workId;
	/**
	 * 场站号
	 */
	private String areaNo;
	/**
	 * 通道号
	 */
	private String laneNo;

	/**
	 * 海关通道号
	 */
	private String customNo;
	/**
	 * 车牌数据 多个车牌 例：粤A1111-粤A7897
	 */
	private String licensePlate;
	/**
	 * 进出闸类型 I 进，E出，
	 */
	private String inExitFlag;
	/**
	 * 数据
	 *
	 */
	private String data;

}
