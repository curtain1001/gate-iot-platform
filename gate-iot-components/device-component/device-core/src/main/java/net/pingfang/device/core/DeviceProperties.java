package net.pingfang.device.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-11 17:36
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProperties implements Serializable {
	private static final long serialVersionUID = -6849794470754667710L;
	/**
	 * 设备id
	 */
	private String deviceId;
	/**
	 * 设备名称
	 */
	private String deviceName;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 设备产品类型
	 */
	private DeviceProduct deviceProduct;
	/**
	 * 网络组件类型
	 */
	private NetworkType networkType;
	/**
	 * 开启关闭
	 */
	private boolean enabled;

	/**
	 * 其他配置(key:network,value:NetworkProperties)
	 */
	private Map<String, Object> configuration = new HashMap<>();

}
