package net.pingfang.network.tcp.parser;

import java.util.Arrays;
import java.util.stream.Collectors;

import net.pingfang.common.utils.JsonUtils;
import net.pingfang.iot.common.customizedsetting.values.Options;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 11:20
 */
public class CustomizedOptions {
	public static String parserTypeOptions() {
		return JsonUtils.toJsonString(Arrays.stream(PayloadParserType.values()) //
				.map(x -> Options.builder() //
						.key(x.getText()) //
						.value(x.name()) //
						.build()) //
				.collect(Collectors.toList()));
	}
}
