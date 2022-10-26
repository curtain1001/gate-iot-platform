package net.pingfang.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * 关锁控制服务
 * </p>
 *
 * @author 王超
 * @since 2022-10-17 10:48
 */
public class CustomsLockControl extends IGateService {
	public CustomsLockControl(RabbitTemplate rabbitTemplate, String fanoutExchange) {
		super(rabbitTemplate, fanoutExchange);
	}

	@Override
	public void post(Long laneId) {
		ContainerOcrService service = new ContainerOcrService(null, null, null, null, null, null, null);
		service.post();
	}

	@Override
	public void post() {

	}
}
