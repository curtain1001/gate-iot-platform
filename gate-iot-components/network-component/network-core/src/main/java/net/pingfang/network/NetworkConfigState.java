package net.pingfang.network;

import org.hswebframework.web.dict.Dict;
import org.hswebframework.web.dict.EnumDict;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Dict("network-config-state")
public enum NetworkConfigState implements EnumDict<String> {
	enabled("已启动", "enabled"), paused("已暂停", "paused"), disabled("已停止", "disabled");

	private final String text;

	private final String value;

}
