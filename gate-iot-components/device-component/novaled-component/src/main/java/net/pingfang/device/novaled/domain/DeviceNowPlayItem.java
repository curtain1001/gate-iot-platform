package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备现在播放项目
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceNowPlayItem {
	private boolean isOpen;
	private int playId;
	private String content;

}
