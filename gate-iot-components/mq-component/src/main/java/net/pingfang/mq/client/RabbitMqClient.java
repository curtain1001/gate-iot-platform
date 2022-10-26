package net.pingfang.mq.client;

import org.springframework.stereotype.Component;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.rabbitmq.RabbitMQClient;
import io.vertx.rabbitmq.RabbitMQConsumer;
import io.vertx.rabbitmq.RabbitMQOptions;
import net.pingfang.mq.MqttMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 14:10
 */
@Component
public class RabbitMqClient implements MqttClient {
	public final Vertx vertx;

	public final RabbitMQClient client;

	public RabbitMqClient(Vertx vertx) {
		this.vertx = vertx;
		RabbitMQOptions config = new RabbitMQOptions();
		// 每个参数都是可选的
		// 如果参数没有被设置，将会使用默认的参数值
		config.setUser("admin");
		config.setPassword("admin");
		config.setHost("114.115.203.170");
		config.setPort(5672);
		config.setVirtualHost("/");
		config.setConnectionTimeout(6000); // in milliseconds
		config.setRequestedHeartbeat(60); // in seconds
		config.setHandshakeTimeout(6000); // in milliseconds
		config.setRequestedChannelMax(5);
		config.setNetworkRecoveryInterval(500); // in milliseconds
		config.setAutomaticRecoveryEnabled(true);
		config.setReconnectAttempts(0);
		client = RabbitMQClient.create(vertx, config);
		// Connect
		client.start(asyncResult -> {
			if (asyncResult.succeeded()) {
				System.out.println("RabbitMQ successfully connected!");
			} else {
				System.out.println("Fail to connect to RabbitMQ " + asyncResult.cause().getMessage());
			}
		});

	}

	@Override
	public Future<RabbitMQConsumer> subscribe(String topics, int qos) {
		// 您可以从队列创建一个消息 stream
		return client.basicConsumer(topics);
	}

	@Override
	public Future<Void> publish(MqttMessage message) {
		Buffer m = Buffer.buffer(" Hello RabbitMQ, from Vert.x !");
		// exchange //routingKey
		return client.basicPublish("", "test_simple_queue", m);
	}
}
