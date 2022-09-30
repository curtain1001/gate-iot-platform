package net.pingfang.network.tcp.server;

import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.network.tcp.parser.DefaultPayloadParserBuilder;
import net.pingfang.network.tcp.parser.PayloadParserType;
import reactor.test.StepVerifier;

@Slf4j
class TcpServerProviderTest {

	static TcpServer tcpServer;

	@BeforeAll
	static void init() {
		TcpServerProperties properties = TcpServerProperties.builder().id("test").port(8080)
				.options(new NetServerOptions()).parserType(PayloadParserType.FIXED_LENGTH)
				.parserConfiguration(Collections.singletonMap("size", 5)).build();

		TcpServerProvider provider = new TcpServerProvider(null, Vertx.vertx(), new DefaultPayloadParserBuilder(),
				null);

		tcpServer = provider.createNetwork(properties);
	}

	@Test
	void test() {

		Vertx.vertx().createNetClient().connect(8080, "localhost", handle -> {
			if (handle.succeeded()) {
				// 模拟粘包，同时发送2个包
				handle.result().write("hellohello", r -> {
					if (r.succeeded()) {
						log.info("tcp客户端消息发送成功");
					} else {
						log.error("tcp客户端消息发送错误", r.cause());
					}
				});
			} else {
				log.error("创建tcp客户端错误", handle.cause());
			}
		});

		tcpServer.subscribe().map(NetworkMessage::payloadAsString).take(2).as(StepVerifier::create)
				.expectNext("hello", "hello")// 收到2个完整的包
				.verifyComplete();
	}

}
