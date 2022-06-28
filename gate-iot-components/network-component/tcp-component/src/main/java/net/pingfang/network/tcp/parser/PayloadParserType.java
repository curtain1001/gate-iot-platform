package net.pingfang.network.tcp.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayloadParserType {

	DIRECT("不处理"),

	FIXED_LENGTH("固定长度"),

	DELIMITED("分隔符"),

	SCRIPT("自定义脚本");

	private final String text;

}
