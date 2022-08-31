package net.pingfang.device.novaled.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备类型
 */
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DeviceType {
	private Date date;
	private boolean isBoxDoorOpen;
	private boolean isScreenPowerOpen;
	private int temperature;
	private boolean isInputSourceInside;
	private int environmentBrightness;
	private int brightnessType; // 1-自动 2-手动 3-定时
	private int screenBrightness;

}
