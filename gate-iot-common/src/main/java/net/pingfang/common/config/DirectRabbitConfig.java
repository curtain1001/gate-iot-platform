package net.pingfang.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 16:52
 */
@Configuration
public class DirectRabbitConfig {
	@Bean
	public TopicExchange rabbitmqTopicExchange() {
		// 配置TopicExchange交换机
		return new TopicExchange(RabbitMQConfig.TOPIC_EXCHANGE_DEVICE_MSG, true, false);
	}

	@Bean
	public Queue topicExchangeQueueA() {
		// 创建队列1
		return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_DEVICE, true, false, false);
	}

	@Bean
	public Queue topicExchangeQueueB() {
		// 创建队列2
		return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_FUN, true, false, false);
	}

	@Bean
	public Queue topicExchangeQueueC() {
		// 创建队列3
		return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_STATUS, true, false, false);
	}

	@Bean
	public Binding bindTopicA() {
		// 队列A绑定到FanoutExchange交换机
		return BindingBuilder.bind(topicExchangeQueueA()).to(rabbitmqTopicExchange()).with(RabbitMQConfig.TOPIC_DEVICE);
	}

	@Bean
	public Binding bindTopicB() {
		// 队列A绑定到FanoutExchange交换机
		return BindingBuilder.bind(topicExchangeQueueB()).to(rabbitmqTopicExchange()).with(RabbitMQConfig.TOPIC_FUN);
	}

	@Bean
	public Binding bindTopicC() {
		// 队列A绑定到FanoutExchange交换机
		return BindingBuilder.bind(topicExchangeQueueA()).to(rabbitmqTopicExchange()).with(RabbitMQConfig.TOPIC_STATUS);
	}

}
