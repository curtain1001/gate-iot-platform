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
 * @since 2022-08-19 11:13
 */
@Slf4j
public class NovaLedPayloadParserBuilder implements PayloadParserBuilderStrategy {

	@Override
	public PayloadParserType getType() {
		return PayloadParserType.LED;
	}

	@Override
	public PayloadParser build(ValueObject config) {
		// 构造parser
		PipePayloadParser parser = new PipePayloadParser();
		parser.delimited(BytesUtils.getBufHexStr(new byte[] { (byte) 0xAA })) //
				.handler(buffer -> { //
					log.info("_____1{}", BytesUtils.getBufHexStr(buffer.getBytes()));
					parser.delimited(BytesUtils.getBufHexStr(new byte[] { (byte) 0xAA })); //
				}).handler(buffer -> {
					log.info("_____2{}", BytesUtils.getBufHexStr(buffer.getBytes()));
					parser.fixed(2);
				}).handler(x -> {
					log.info("_____3{}", BytesUtils.getBufHexStr(x.getBytes()));
					parser.result(x);
				}) //
				.complete(); //
		return parser;
	}

}
