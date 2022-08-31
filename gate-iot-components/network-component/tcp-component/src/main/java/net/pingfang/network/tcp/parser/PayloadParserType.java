package net.pingfang.network.tcp.parser;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayloadParserType {

	DIRECT("不处理"),

	FIXED_LENGTH("固定长度"),

	DELIMITED("分隔符"),

	SCRIPT("自定义脚本"), //

	PLC("自定义PLC设备"), //

	LED("诺瓦LED");

	private final String text;

	static {
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				return PayloadParserType.valueOf(value.toString());
			}
		}, PayloadParserType.class);
	}
}
