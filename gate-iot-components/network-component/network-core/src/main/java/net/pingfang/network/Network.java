package net.pingfang.network;

import net.pingfang.iot.common.NetworkSession;
import net.pingfang.iot.common.network.NetworkType;

/**
 * 网络组件，所有网络相关实例根接口
 *
 * @author zhouhao
 * @version 1.0
 * @since 1.0
 */
public interface Network extends NetworkSession {

	/**
	 * ID唯一标识
	 *
	 * @return ID
	 */
	String getId();

	/**
	 * @return 网络类型
	 * @see DefaultNetworkType
	 */
	NetworkType getType();

	/**
	 * 当{@link Network#isAlive()}为false是,是否自动重新加载.
	 *
	 * @return 是否重新加载
	 * @see NetworkProvider#reload(Network, Object)
	 */
	boolean isAutoReload();
}
