package net.pingfang.network.tcp.parser;

import net.pingfang.iot.common.ValueObject;

public interface PayloadParserBuilder {

	PayloadParser build(PayloadParserType type, ValueObject configuration);

}
