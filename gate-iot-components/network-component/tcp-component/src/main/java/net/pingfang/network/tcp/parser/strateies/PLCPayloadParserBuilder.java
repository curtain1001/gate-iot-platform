package net.pingfang.network.tcp.parser.strateies;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.tcp.parser.PayloadParserBuilderStrategy;
import net.pingfang.network.tcp.parser.PayloadParserType;

/**
 * @author ηθΆ
 * @description TODO
 * @date 2022-07-25 10:17
 */
@Slf4j
public class PLCPayloadParserBuilder implements PayloadParserBuilderStrategy {

	@Override
	public PayloadParserType getType() {
		return PayloadParserType.PLC;
	}

	@Override
	public PayloadParser build(ValueObject config) {
		PipePayloadParser parser = new PipePayloadParser();
		parser.fixed(4).handler(buffer -> {
			byte head = buffer.getByte(0);
			if (head != (byte) 0xFE) {
				log.info("---ignore---γ" + head);
				return;
			}
			parser.fixed(2);
		}).handler(buffer -> parser.result(buffer).complete());
		return parser;
	}
}
