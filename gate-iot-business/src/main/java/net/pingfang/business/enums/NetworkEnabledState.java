package net.pingfang.business.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NetworkEnabledState {
	enabled("已启动", 0), disabled("已停止", 1);

	private final String text;

	private final int value;

}
