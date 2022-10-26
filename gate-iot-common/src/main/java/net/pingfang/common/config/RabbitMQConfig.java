package net.pingfang.common.config;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 21:38
 */
public class RabbitMQConfig {

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机名称
	 */
	public static final String TOPIC_EXCHANGE_DEVICE_MSG = "topic.exchange.device.msg";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列A的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_DEVICE = "topic.queue.device";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列B的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_FUN = "topic.queue.fun";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列C的名称
	 */
	public static final String TOPIC_EXCHANGE_QUEUE_STATUS = "topic.queue.status";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列A的名称
	 */
	public static final String TOPIC_DEVICE = "topic.device.#";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列B的名称
	 */
	public static final String TOPIC_FUN = "topic.fun.#";

	/**
	 * RabbitMQ的TOPIC_EXCHANGE交换机的队列C的名称
	 */
	public static final String TOPIC_STATUS = "topic.status.#";
}
