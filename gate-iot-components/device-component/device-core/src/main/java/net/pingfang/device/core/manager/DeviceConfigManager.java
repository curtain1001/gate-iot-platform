package net.pingfang.device.core.manager;

import java.util.List;

import net.pingfang.device.core.DeviceProperties;

/**
 * 网络组件配置管理器
 *
 * @author zhouhao
 */
public interface DeviceConfigManager {

	DeviceProperties getProperties(String id);

	List<DeviceProperties> getProperties();

}
