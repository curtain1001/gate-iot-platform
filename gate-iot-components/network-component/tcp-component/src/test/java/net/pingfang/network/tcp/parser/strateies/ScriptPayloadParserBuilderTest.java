package net.pingfang.network.tcp.parser.strateies;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import io.vertx.core.buffer.Buffer;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParser;
import net.pingfang.network.utils.BytesUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ScriptPayloadParserBuilderTest {

	@Test
	void testSplicingUnpack() {
		ScriptPayloadParserBuilder builder = new ScriptPayloadParserBuilder();
		Map<String, Object> config = new HashMap<>();
		config.put("script", //
				"\n" + "var BytesUtils = net.pingfang.network.utils.BytesUtils;\n" //
						+ "parser.fixed(4)\n" //
						+ "       .handler(function(buffer){\n" //
						+ "            		var len = BytesUtils.highBytesToInt(buffer.getBytes());\n"
						+ "            		parser.fixed(len);\n" //
						+ "        		})\n" //
						+ "       .handler(function(buffer){\n" //
						+ "            		parser.result(buffer.toString(\"UTF-8\"))\n" //
						+ "                   .complete();\n" //
						+ "        });"); //
		config.put("lang", "javascript");
		PayloadParser parser = builder.build(ValueObject.of(config));

		parser.handlePayload().doOnSubscribe(sb -> {
			Mono.delay(Duration.ofMillis(100)).subscribe(r -> {
				Buffer buffer = Buffer.buffer(BytesUtils.toHighBytes(5));
				buffer.appendString("1234");
				parser.handle(buffer);
				parser.handle(Buffer.buffer("5"));

				parser.handle(Buffer.buffer(new byte[] { 5, 0 }));
				parser.handle(Buffer.buffer(new byte[] { 0, 0 }).appendString("12"));
				parser.handle(Buffer.buffer("345"));
			});
		}).take(2).map(bf -> bf.toString(StandardCharsets.UTF_8)).as(StepVerifier::create).expectNext("12345", "12345")
				.verifyComplete();
	}

	@Test
	void testDirect() {
		ScriptPayloadParserBuilder builder = new ScriptPayloadParserBuilder();
		Map<String, Object> config = new HashMap<>();
		config.put("script", "\n"//
				+ "var cache = parser.newBuffer();\n" //
				+ "parser.direct(function(buffer){\n" //
				+ "            cache.appendBuffer(buffer);\n"//
				+ "            if(cache.length()>=16){\n" //
				+ "               var result = cache;\n" //
				+ "               cache = parser.newBuffer();\n" //
				+ "               parser.result(result)\n"//
				+ "                     .complete(); \n" //
				+ "             }\n" //
				+ "             return null;\n" //
				+ "        });");//
		config.put("lang", "javascript");
		System.out.println(config.get("script"));
		PayloadParser parser = builder.build(ValueObject.of(config));

		parser.handlePayload().doOnSubscribe(sb -> {
			Mono.delay(Duration.ofMillis(100)).subscribe(r -> {
				parser.handle(Buffer.buffer(new byte[] { 0, 1, 2, 3 }));
				parser.handle(Buffer.buffer(new byte[] { 0, 1, 2, 3 }));
				parser.handle(Buffer.buffer(new byte[] { 0, 1, 2, 3 }));
				parser.handle(Buffer.buffer(new byte[] { 0, 1, 2, 3 }));
			});
		}).take(1).map(Buffer::length).as(StepVerifier::create).expectNext(16).verifyComplete();
	}

}
