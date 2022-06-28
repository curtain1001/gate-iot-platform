package net.pingfang.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NetworkConfigState {
	enabled("已启动", "enabled"), paused("已暂停", "paused"), disabled("已停止", "disabled");

	private final String text;

	private final String value;

}
