package net.pingfang.network.nova;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 20:03
 */

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DefaultProperties {
	/**
	 * 设备名称
	 */
	String deviceName;

	/**
	 * 设备密码
	 */
	String password;
	/**
	 * 是否开启DES加密
	 */
	boolean isDesOpen;
	/**
	 * 文件是否采用MD5加密
	 */
	boolean isMD5Open;
	/**
	 * des密码
	 */
	String desPassword;

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

	int imgparamN;
}
