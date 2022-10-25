package net.pingfang.network.nova;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 19:49
 */
@Data
public class NovaProperties {
	/**
	 * 停留时间
	 */
	int dwellTime;
	/**
	 * 入屏方式
	 */
	int startDisplayMode;
	/**
	 * 出屏方式
	 */
	int endDisplayMode;
	/**
	 * 入屏速度
	 */
	int startSpeed;
	/**
	 * 闪烁速度
	 *
	 */
	int flickerSpeed;

	/**
	 * 显示区域宽度
	 */
	int displayWidth;

	/**
	 * 显示区域高度
	 */
	int displayHigh;

}
