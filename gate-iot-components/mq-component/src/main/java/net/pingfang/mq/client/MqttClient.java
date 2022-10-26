package net.pingfang.mq.client;

import io.vertx.core.Future;
import io.vertx.rabbitmq.RabbitMQConsumer;
import net.pingfang.mq.MqttMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 14:06
 */
public interface MqttClient {

	default Future<RabbitMQConsumer> subscribe(String topics) {
		return subscribe(topics, 0);
	}

	Future<RabbitMQConsumer> subscribe(String topics, int qos);

	Future<Void> publish(MqttMessage message);
}
