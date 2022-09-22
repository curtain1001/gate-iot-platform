package net.pingfang.business.values;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 集装箱残损图片标注
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:22
 */
@Data
@Builder(toBuilder = true)
public class ContainerDamageMark {
	/**
	 * 残损局域
	 */
	final String damageArea;
	/**
	 * 残损描述
	 */
	final String damageDescribe;
	/**
	 * 残损宽度
	 */
	final String damageWidth;
	/**
	 * 残损高度
	 */
	final String damageHeight;
	/**
	 * 残损图片
	 */
	final String damagePictureUrl;
	/**
	 * 残损位置
	 */
	final String damagePosition;
}
