package net.pingfang.device.novaled.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 现在设备播放列表
 */
@AllArgsConstructor
@Data
@Builder
public class DeviceNowPlayList {
	private int playId;
	private String content;

}
