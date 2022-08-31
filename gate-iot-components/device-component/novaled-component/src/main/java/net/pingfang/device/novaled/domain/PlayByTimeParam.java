package net.pingfang.device.novaled.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 播放时间参数
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayByTimeParam {
	private Date start;
	private Date end;
	private int id;

}
