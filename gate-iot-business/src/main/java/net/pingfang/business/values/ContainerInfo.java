package net.pingfang.business.values;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 箱信息
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:06
 */
@Data
@Builder(toBuilder = true)
public class ContainerInfo {
	/**
	 * 箱号
	 */
	final String containerNo;
	/**
	 * 箱型
	 */
	final String containerIso;
	/**
	 * 验箱结果
	 */
	final boolean containerVerification;
	/**
	 * 箱门方向 F 前，B后
	 */
	final String containerDoorDirection;

	/**
	 * 最大箱重
	 */
	final BigDecimal containerMaximumWeight;
	/**
	 * 可信度
	 */
	final String containerReliability;
	/**
	 * 位置 F 前箱，B后箱
	 */
	final String containerPosition;
}
