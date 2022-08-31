package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 设备尺寸
 */
@AllArgsConstructor
@Builder
@Data
public class DeviceSize {
	private int width;
	private int height;

}
