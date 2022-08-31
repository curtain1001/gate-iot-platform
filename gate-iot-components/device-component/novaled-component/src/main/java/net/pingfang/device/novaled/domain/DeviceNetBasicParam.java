package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备网络基本参数
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeviceNetBasicParam {
	private String deviceIp; // "192.168.0.220"
	private int port;// 500
	private String mark;// 255.255.255.0
	private String gate;// 192.168.0.1
	private String serviceIp;// 当卡为客户端时候，这个就是服务端对应IP；当卡为服务端时，无效。

}
