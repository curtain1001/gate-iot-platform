package net.pingfang.network.tcp.parser.strateies;

import lombok.SneakyThrows;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.DirectRecordParser;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.tcp.parser.PayloadParserBuilderStrategy;
import net.pingfang.network.tcp.parser.PayloadParserType;

public class DirectPayloadParserBuilder implements PayloadParserBuilderStrategy {

	@Override
	public PayloadParserType getType() {
		return PayloadParserType.DIRECT;
	}

	@Override
	@SneakyThrows
	public PayloadParser build(ValueObject config) {
		return new DirectRecordParser();
	}
}
