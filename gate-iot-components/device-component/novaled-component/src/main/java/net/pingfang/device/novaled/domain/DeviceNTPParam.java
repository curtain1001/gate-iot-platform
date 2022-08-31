package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 设备NTP参数
 */
@Data
@Builder
@AllArgsConstructor
public class DeviceNTPParam {
	private boolean isOpen;
	private String addr; // utf-8 ntp服务器地址

}
