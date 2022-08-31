package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 亮度
 */
@AllArgsConstructor
@Builder
@Data
public class BrightnessItem {
	private int environment;// 环境亮度
	private int screen;// 屏体亮度

}
