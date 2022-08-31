package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 设备秘密参数
 */

@AllArgsConstructor
@Data
@Builder
public class DeviceSecretParam {
	private String password; // viplex账号密码 默认123456
	private boolean isDesOpen;
	private boolean isMD5Open;
	private String desPassword; // des密码 必须是8位

}
