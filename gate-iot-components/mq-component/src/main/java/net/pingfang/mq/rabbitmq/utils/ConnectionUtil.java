package net.pingfang.mq.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 9:37
 */

@Slf4j
public class ConnectionUtil {
//	Direct：完全根据key进行投递的，例如，绑定时设置了routing key为”abc”，那么客户端提交的消息，只有设置了key为”abc”的才会投递到队列。
//	Topic：对key进行模式匹配后进行投递，符号”#”匹配一个或多个词，符号”*”匹配正好一个词。例如”abc.#”匹配”abc.def.ghi”，”abc.*”只匹配”abc.def”。
//	Fanout：不需要key，它采取广播模式，一个消息进来时，投递到与该交换机绑定的所有队列。
//	Headers:我们可以不考虑它

	public static Connection getConnection() {
		try {
			Connection connection = null;
			// 定义一个连接工厂
			ConnectionFactory factory = new ConnectionFactory();
			// 设置服务端地址（域名地址/ip）
			factory.setHost("114.115.203.170");
			// 设置服务器端口号
			factory.setPort(5672);
			// 设置虚拟主机(相当于数据库中的库)
			factory.setVirtualHost("/");
			// 设置用户名
			factory.setUsername("admin");
			// 设置密码
			factory.setPassword("admin");
			factory.setConnectionTimeout(30000000);
			factory.setHandshakeTimeout(300000000);// 设置握手超时时间
			connection = factory.newConnection();
			return connection;
		} catch (Exception e) {
			return null;
		}
	}
}
