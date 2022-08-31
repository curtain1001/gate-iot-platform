package net.pingfang.device.novaled.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 亮度随时间参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrightnessByTimeParam {
	Date start;
	Date end;
	int brightness;
}
