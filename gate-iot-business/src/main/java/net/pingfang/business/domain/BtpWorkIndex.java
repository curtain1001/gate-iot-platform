package net.pingfang.business.domain;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.annotation.FieldName;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.common.utils.Column;
import net.pingfang.common.utils.FieldNameUtils;

/**
 * <p>
 * 作业流程
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:01
 */
@SuperBuilder(toBuilder = true)
@TableName(value = "btp_work_index")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BtpWorkIndex extends BaseEntity {

	private static final long serialVersionUID = -1252932924613573523L;
	@TableId(type = IdType.AUTO)
	private Long dataId;

	@FieldName(value = "作业号")
	private String workId;
	/**
	 * 场站号
	 */
	@FieldName(value = "场站号")
	private String areaNo;
	/**
	 * 通道号
	 */
	@FieldName(value = "通道号")
	private String laneNo;
	/**
	 * 海关通道号
	 */
	@FieldName(value = "海关通道号")
	private String customNo;
	/**
	 * 进出闸类型 I 进，E出，
	 */
	@FieldName(value = "进出闸类型")
	private String inExitFlag;
	/**
	 * 采集时间
	 */
	@FieldName(value = "采集时间")
	private String collectionTime;
	/**
	 * 车牌
	 */
	@FieldName(value = "车牌")
	private String licensePlate;
	/**
	 * 前箱号
	 */
	@FieldName(value = "前箱号")
	private String forwardContainerNo;
	/**
	 * 后箱号
	 */
	@FieldName(value = "后箱号")
	private String backContainerNo;

	/**
	 * 车架号
	 */
	@FieldName(value = "车架号")
	private String framePlate;
	/**
	 * 车轮毂数
	 */
	@FieldName(value = "车轮毂数")
	private Integer wheelHubTotal;

	/**
	 * 车轮毂识别区域
	 */
	@FieldName(value = "车轮毂识别区域")
	private String wheelHubMark;
	/**
	 * 地磅重量
	 */
	@FieldName(value = "地磅重量")
	private BigDecimal weight;

	public static void main(String[] args) {
		List<Column> columnList = FieldNameUtils.getColumns(BtpWorkIndex.class);
		System.out.println(columnList.toString());
	}
}
