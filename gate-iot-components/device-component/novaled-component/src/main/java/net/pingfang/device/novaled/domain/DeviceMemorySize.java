package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备内存大小
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceMemorySize {
	private int totolSize;// 单位MB
	private int remainSize;// 单位MB

}
