package net.pingfang.network.tcp.parser.strateies;

import org.apache.commons.lang3.StringEscapeUtils;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.tcp.parser.PayloadParserBuilderStrategy;
import net.pingfang.network.tcp.parser.PayloadParserType;

/**
 * <p>
 * 箱号识别报文解析
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 18:19
 */
@Slf4j
public class OcrPayloadParserBuilder implements PayloadParserBuilderStrategy {

	@Override
	public PayloadParserType getType() {
		return PayloadParserType.OCR;
	}

	@Override
	public PayloadParser build(ValueObject config) {
		PipePayloadParser parser = new PipePayloadParser();
		parser.delimited(StringEscapeUtils.unescapeJava("<Vehicle>"))//
				.handler(buffer -> { //
					parser.delimited(StringEscapeUtils.unescapeJava("</Vehicle>"));
				}).handler(buffer -> {
					Buffer b = Buffer.buffer("<Vehicle>");
					b.appendBuffer(buffer);
					b.appendBuffer(Buffer.buffer("</Vehicle>"));
					parser.result(b).complete();
				}).complete();
		return parser;
	}
}
