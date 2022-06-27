package net.pingfang.network.tcp.parser.strateies;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import io.vertx.core.buffer.Buffer;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;

class FixLengthPayloadParserBuilderTest {

	@Test
	void testFixLength() {
		FixLengthPayloadParserBuilder builder = new FixLengthPayloadParserBuilder();
		PayloadParser parser = builder.build(ValueObject.of(Collections.singletonMap("size", 5)));
		List<String> arr = new ArrayList<>();

		parser.handlePayload().map(buffer -> buffer.toString(StandardCharsets.UTF_8)).subscribe(arr::add);

		parser.handle(Buffer.buffer("123"));
		parser.handle(Buffer.buffer("4567"));
		parser.handle(Buffer.buffer("890"));

		Assert.assertArrayEquals(arr.toArray(), new Object[] { "12345", "67890" });

	}

	@Test
	void testDelimited() {
		DelimitedPayloadParserBuilder builder = new DelimitedPayloadParserBuilder();
		PayloadParser parser = builder.build(ValueObject.of(Collections.singletonMap("delimited", "@@")));
		List<String> arr = new ArrayList<>();

		parser.handlePayload().map(buffer -> buffer.toString(StandardCharsets.UTF_8)).subscribe(arr::add);

		parser.handle(Buffer.buffer("123"));
		parser.handle(Buffer.buffer("45@@67"));
		parser.handle(Buffer.buffer("890@@111"));

		Assert.assertArrayEquals(arr.toArray(), new Object[] { "12345", "67890" });

	}
}
