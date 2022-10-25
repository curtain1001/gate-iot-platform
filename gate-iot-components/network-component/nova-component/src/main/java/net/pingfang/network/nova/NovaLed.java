package net.pingfang.network.nova;

import java.util.Date;
import java.util.List;

import net.pingfang.network.Network;
import nova.traffic.been.DeviceNetBasicParam;
import nova.traffic.been.DeviceNowPlayItem;
import nova.traffic.been.DeviceNowPlayList;
import nova.traffic.been.DevicePowerByTimeParam;
import nova.traffic.been.DeviceSecretParam;
import nova.traffic.been.DeviceType;
import nova.traffic.been.PlayByTimeParam;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 14:58
 */
public interface NovaLed extends Network {
	/**
	 * 初始化
	 */
	void init();

	/**
	 * 获取设备名称
	 *
	 * @return 设备名称
	 */
	String getDeviceName();

	/**
	 * 设置设备名称
	 *
	 * @param name 设备名称
	 * @return 1 成功 小于0 错误 见错误码
	 */
	int setDeviceName(String name);

	int setDeviceSecretParam(DeviceSecretParam param);

	/**
	 * 消息通讯是否采用DES加密的密码
	 *
	 * @param password 密码
	 * @return
	 */
	boolean NovaTrafficSetPassword(String password);

	/**
	 * 发送播放文件
	 *
	 * @return
	 */
	int sendPlayList(int id, String content);

	/**
	 * 开关屏
	 */
	int switchPower(boolean power);

	/**
	 * 清理文件（其他数字按1处理） 0: 所有媒体文件 1: 所有无效媒体文件 2: 所有文件 3: 升级包
	 */
	int cleanUpFiles(int type);

	/**
	 * 电源开关
	 */
	int setBoardPowerAndScreenPower(boolean power);

	DeviceType getDeviceType();

	/**
	 * 设置时间
	 *
	 * @param date
	 * @return
	 */
	int setTime(Date date);

	/**
	 * 重启
	 *
	 * @return
	 */
	int restart();

	/**
	 * 恢复默认ip 192.168.0.220
	 */
	void resetDeviceIpToDefault();

	/**
	 * 设备屏体基本参数
	 *
	 * @param mDeviceBasicParam
	 */
	void setDeviceNetBasicParam(DeviceNetBasicParam mDeviceBasicParam);

	/**
	 * 获取当前播放内容
	 *
	 * @return
	 */
	DeviceNowPlayItem getNowPlayContent();

	/**
	 * 获取当前播放列表的全部内容
	 *
	 * @return
	 */
	DeviceNowPlayList getNowPlayAllContent();

	/**
	 * 设置定时开关屏
	 *
	 * @param list
	 * @return
	 */
	int setDevicePowerByTimeList(List<DevicePowerByTimeParam> list);

	int setPlayByTimeList(List<PlayByTimeParam> list);
}
