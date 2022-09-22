package net.pingfang.network;

import java.util.List;

import net.pingfang.iot.common.network.NetworkType;

/**
 * 网络组件配置管理器
 *
 * @author zhouhao
 */
public interface NetworkConfigManager {

	NetworkProperties getConfig(NetworkType networkType, String id);

	NetworkProperties getConfig(String configId);

	List<NetworkProperties> getConfig();

	void update(String configId, String state);

}
