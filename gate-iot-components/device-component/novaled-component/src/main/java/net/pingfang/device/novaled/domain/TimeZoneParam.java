package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 时区参数
 */
@AllArgsConstructor
@Data
@Builder
public class TimeZoneParam {
	private long utcTime;// 1565409600000
	private String timeZoneId;// Asia/Shanghai
	private String gmt;// GMT+08:00

}
