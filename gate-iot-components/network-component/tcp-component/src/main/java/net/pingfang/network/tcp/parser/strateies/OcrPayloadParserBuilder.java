package net.pingfang.network.tcp.parser.strateies;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.tcp.parser.PayloadParserBuilderStrategy;
import net.pingfang.network.tcp.parser.PayloadParserType;
import net.pingfang.network.utils.BytesUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 18:19
 */
@Slf4j
public class OcrPayloadParserBuilder implements PayloadParserBuilderStrategy {

	private static final int MAGIC_NUMBER = 0x06;

	@Override
	public PayloadParserType getType() {
		return PayloadParserType.OCR;
	}

	@Override
	public PayloadParser build(ValueObject config) {
		PipePayloadParser parser = new PipePayloadParser();
		int size = -1;
		parser.delimited(BytesUtils.getBufHexStr(new byte[] { MAGIC_NUMBER })).handler(buffer -> {
			byte head = buffer.getByte(4);
			if (head != (byte) 0xFE) {
				log.info("---ignore---》" + head);
				return;
			}
			parser.fixed(2);
		}).handler(buffer -> parser.result(buffer).complete());
		return parser;
	}
}
