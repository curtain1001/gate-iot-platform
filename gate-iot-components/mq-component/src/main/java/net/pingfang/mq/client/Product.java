package net.pingfang.mq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 16:26
 */
public class Product {
	// exchange名字
	public static String EXCHANGE_NAME = "exchange";

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

			// 2.为通道声明exchange和exchange的类型
			channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

			String msg = " hello rabbitmq, this is publish/subscribe mode";
			// 3.发送消息到指定的exchange,队列指定为空,由exchange根据情况判断需要发送到哪些队列
			channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
			System.out.println("product send a msg: " + msg);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		} finally {
			// 4.关闭连接
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException | TimeoutException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
