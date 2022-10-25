package net.pingfang.network.nova.media;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-21 15:27
 */
@Data
public class VideoParam extends MediaParam {
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 播放次数
	 */
	private int playNum;
}
