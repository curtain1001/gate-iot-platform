package net.pingfang.network.nova.media;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-21 15:19
 */
@Data
public class MediaParam {
	/**
	 * x 坐标
	 */
	int x;
	/**
	 * y 坐标
	 */
	int y;

	/**
	 * 闪烁
	 */
	int flicker;

	/**
	 * 显示区域宽度
	 */
	private int displayWidth;

	/**
	 * 显示区域高度
	 */
	private int displayHigh;

}
