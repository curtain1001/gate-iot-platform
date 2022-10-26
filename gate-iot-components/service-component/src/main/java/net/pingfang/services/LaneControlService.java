package net.pingfang.services;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import net.pingfang.common.config.RabbitMQConfig;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.iot.common.message.DeviceCollectMessage;
import net.pingfang.services.events.EndGateFlowEvent;
import net.pingfang.services.events.StartGateFlowEvent;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-14 17:39
 */
@Component
public class LaneControlService {
	@Resource
	EventBusCenter eventBusCenter;

	@RabbitHandler
	@RabbitListener(bindings = { @QueueBinding(value = @Queue(), // 如果不括号中不指定队列名称，那么这时候创建的就是临时队列，当消费者连接断开的时候，该队列就会消失
			exchange = @Exchange(value = RabbitMQConfig.TOPIC_EXCHANGE_DEVICE_MSG, type = "topic"), key = "topic.device.PLC") })
	public void process(DeviceCollectMessage collectMessage, Channel channel, Message message) throws IOException {
		if (collectMessage.getPayload().equals("FE0000FF")) {
			eventBusCenter.postSync(StartGateFlowEvent.builder() //
					.laneId(collectMessage.getLaneId())//
					.build());
		}
		if (collectMessage.getPayload().equals("FE0100FF")) {
			eventBusCenter.postSync(EndGateFlowEvent.builder()//
					.laneId(collectMessage.getLaneId())//
					.build());
		}
		System.out.println("消费者接收到的消息是" + JsonUtils.toJsonString(collectMessage));
		// 由于配置设置了手动应答，所以这里要进行一个手动应答。注意：如果设置了自动应答，这里又进行手动应答，会出现double ack，那么程序会报错。
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
	}
}
