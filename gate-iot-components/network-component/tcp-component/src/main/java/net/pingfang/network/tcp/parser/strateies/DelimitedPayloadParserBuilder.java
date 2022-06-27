package net.pingfang.network.tcp.parser.strateies;

import org.apache.commons.lang3.StringEscapeUtils;

import io.vertx.core.parsetools.RecordParser;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParserType;

public class DelimitedPayloadParserBuilder extends VertxPayloadParserBuilder {
	@Override
	public PayloadParserType getType() {
		return PayloadParserType.DELIMITED;
	}

	@Override
	protected RecordParser createParser(ValueObject config) {

		return RecordParser.newDelimited(StringEscapeUtils.unescapeJava(config.getString("delimited")
				.orElseThrow(() -> new IllegalArgumentException("delimited can not be null"))));
	}

}
