package net.pingfang.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import net.pingfang.common.utils.JsonUtils;
import net.pingfang.services.values.ServiceMessage;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 17:18
 */
public abstract class IGateService {
	private RabbitTemplate rabbitTemplate;
	private String fanoutExchange;
	private ServiceProduct serviceProduct;

	public IGateService(RabbitTemplate rabbitTemplate, String fanoutExchange) {
		this.rabbitTemplate = rabbitTemplate;
		this.fanoutExchange = fanoutExchange;

	}

	/**
	 * 手动上报
	 *
	 * @param laneId
	 */
	public abstract void post(Long laneId);

	/**
	 * 自动上报
	 *
	 */
	public abstract void post();

	public void send(ServiceMessage msg) {
		rabbitTemplate.convertAndSend(fanoutExchange, "", JsonUtils.toJsonString(msg));
	}
}
