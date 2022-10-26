package net.pingfang.mq.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.rabbitmq.RabbitMQConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 14:36
 */
@Slf4j
public class RabbitMqClientTest {
	public static RabbitMqClient client;

//	@BeforeAll
	static void init() throws InterruptedException {
		Vertx vertx = Vertx.vertx();
		client = new RabbitMqClient(vertx);
		CountDownLatch latch = new CountDownLatch(1);
		latch.await(5, TimeUnit.SECONDS);

		client.publish(null);
	}

//	@Test
	void mq() throws InterruptedException {

		Future<RabbitMQConsumer> r = client.subscribe("test_simple_queue");
		if (r.succeeded()) {
			System.out.println("RabbitMQ consumer created !");
			RabbitMQConsumer mqConsumer = r.result();
			mqConsumer.handler(message -> {
				System.out.println("Got message: " + message.body().toString());
			});
		} else {
			r.result();
		}
	}
}
