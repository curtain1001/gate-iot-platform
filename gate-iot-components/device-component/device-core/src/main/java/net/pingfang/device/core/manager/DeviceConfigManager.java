package net.pingfang.device.core.manager;

import java.util.List;

import net.pingfang.device.core.DeviceProperties;
import net.pingfang.iot.common.product.Product;

/**
 * 网络组件配置管理器
 *
 * @author zhouhao
 */
public interface DeviceConfigManager {

	DeviceProperties getProperties(Product product, String id);

	List<DeviceProperties> getProperties();

}
