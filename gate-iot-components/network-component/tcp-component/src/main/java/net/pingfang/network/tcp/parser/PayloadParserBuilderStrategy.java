package net.pingfang.network.tcp.parser;

import net.pingfang.iot.common.ValueObject;

public interface PayloadParserBuilderStrategy {
	PayloadParserType getType();

	PayloadParser build(ValueObject config);
}
