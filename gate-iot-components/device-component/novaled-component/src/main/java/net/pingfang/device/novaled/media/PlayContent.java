package net.pingfang.device.novaled.media;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 15:20
 */
@Data
public class PlayContent {
	private PlayType playType;
	/**
	 * 列表中的序号 1-24
	 */
	private int item;
	/**
	 * 停留时间
	 */
	private int dwellTime;
	/**
	 * 入屏方式
	 */
	private int startDisplayMode;
	/**
	 * 出屏方式
	 */
	private int endDisplayMode;
	/**
	 * 入屏速度
	 */
	private int startSpeed;
	/**
	 * 闪烁速度
	 *
	 */
	private int flickerSpeed;
	/**
	 * 闪烁次数
	 */
	private int flickerNum;

	/**
	 * 播放次数
	 */
	private int playNum;

}
