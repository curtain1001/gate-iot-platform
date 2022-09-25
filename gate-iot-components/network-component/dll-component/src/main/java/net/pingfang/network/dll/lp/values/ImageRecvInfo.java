package net.pingfang.network.dll.lp.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author 王超
 * @description TODO
 * @date 2022-04-22 17:10
 */
@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class ImageRecvInfo {
	/**
	 * 图片的宽度，单位:像素
	 */
	String usWidth;
	/**
	 * 图片的高度，单位:像素
	 */
	String usHeight;
	/**
	 * 车身颜色，E_ColorType
	 */
	String ucVehicleColor;
	/**
	 * 车标，E_VehicleFlag
	 */
	String ucVehicleBrand;
	/**
	 * 车型(1大2中3小)，,目前根据车牌颜色来区分大小车，蓝牌为小车，黄牌为大车
	 */
	String ucVehicleSize;
	/**
	 * 车牌颜色
	 */
	String ucPlateColor;
	/**
	 * 车牌，若为'\0'，表示无效GB2312编码
	 */
	String szLprResult;
	/**
	 * 车牌位置，左上角(0, 1), 右下角(2,3)
	 */
	String usLpBox;
	/**
	 * 车牌类型
	 */
	String ucLprType;
	/**
	 * 单位km/h
	 */
	String usSpeed;
	/**
	 * 抓拍模式,
	 */
	String ucSnapType;
	/**
	 * 车牌防伪 0未知1异常2正常
	 */
	String ucHaveVehicle;
	/**
	 * 图片抓拍时间:格式YYYYMMDDHHMMSSmmm(年月日时分秒毫秒)
	 */
	String acSnapTime;
	/**
	 * 违法代码
	 */
	String ucViolateCode;
	/**
	 * 车道号,从0开始编码
	 */
	String ucLaneNo;
	/**
	 * 检测到的车辆id，若为同一辆车，则id相同
	 */
	String uiVehicleId;
	/**
	 * 车牌识别可行度
	 */
	String ucScore;
	/**
	 * 行车方向E_Direction
	 */
	String ucDirection;
	/**
	 * 该车辆抓拍总张数
	 */
	String ucTotalNum;
	/**
	 * 当前抓拍第几张，从0开始编号
	 */
	String ucSnapshotIndex;
	/**
	 * 全景图片
	 */
	byte[] panorama;
	/**
	 * 车辆图片
	 */
	byte[] vehicle;

}
