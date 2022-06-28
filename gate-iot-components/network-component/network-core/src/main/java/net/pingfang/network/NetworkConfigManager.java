package net.pingfang.network;

/**
 * 网络组件配置管理器
 *
 * @author zhouhao
 */
public interface NetworkConfigManager {

	NetworkProperties getConfig(NetworkType networkType, String id);

}
