package net.pingfang.mq.rabbitmq.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 9:40
 */
public class Send {
	// 队列名称
	private static final String QUEUE_NAME = "test_simple_queue";

	public static void main(String[] args) {
		try {
			// 获取连接
			Connection connection = ConnectionUtil.getConnection();
			// 从连接中获取一个通道
			Channel channel = connection.createChannel();
			// 声明队列
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "This is simple queue";
			// 发送消息
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes("utf-8"));
			System.out.println("[send]：" + message);
			channel.close();
			connection.close();
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}
}
