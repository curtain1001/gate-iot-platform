package net.pingfang.mq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 16:27
 */
public class Consumer2 {
	public static void main(String[] args) {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("114.115.203.170");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		Connection connection = null;
		Channel channel = null;
		try {
			// 1.创建连接和通道
			connection = factory.newConnection();
			channel = connection.createChannel();

			// 2.为通道声明exchange以及exchange类型
			channel.exchangeDeclare(Product.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

			// 3.创建随机名字的队列
			String queueName = channel.queueDeclare().getQueue();

			// 4.建立exchange和队列的绑定关系
			channel.queueBind(queueName, Product.EXCHANGE_NAME, "");
			System.out.println(" **** Consumer1 keep alive ,waiting for messages, and then deal them");
			// 5.通过回调生成消费者并进行监听
			Consumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope,
						com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {

					// 获取消息内容然后处理
					String msg = new String(body, "UTF-8");
					System.out.println("*********** Consumer1" + " get message :[" + msg + "]");
				}
			};
			// 6.消费消息
			channel.basicConsume(queueName, true, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
	}
}
