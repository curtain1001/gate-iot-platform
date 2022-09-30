package net.pingfang.network.tcp.client;

import java.util.Collections;

import org.junit.Test;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClientOptions;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.network.tcp.parser.DefaultPayloadParserBuilder;
import net.pingfang.network.tcp.parser.PayloadParserType;
import reactor.test.StepVerifier;

//@RunWith(SpringRunner.class)
@Slf4j
public class VertxTcpClientProviderTest {

	@Test
	public void test() {
		Vertx vertx = Vertx.vertx();

		vertx.createNetServer().connectHandler(socket -> {
			socket.write("tes");
			socket.write("ttest");
		}).listen(12311);

		VertxTcpClientProvider provider = new VertxTcpClientProvider(null, vertx, new DefaultPayloadParserBuilder());

		TcpClientProperties properties = new TcpClientProperties();
		properties.setHost("127.0.0.1");
		properties.setPort(12311);
		properties.setParserType(PayloadParserType.FIXED_LENGTH);
		properties.setParserConfiguration(Collections.singletonMap("size", 4));
		properties.setOptions(new NetClientOptions());

		provider.createNetwork(properties).subscribe().map(NetworkMessage::payloadAsString).take(2)
				.as(StepVerifier::create).expectNext("test", "test").verifyComplete();

	}

}
