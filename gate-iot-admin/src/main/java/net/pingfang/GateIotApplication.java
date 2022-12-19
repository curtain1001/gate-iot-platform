package net.pingfang;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.pingfang.common.manager.AsyncManager;
import net.pingfang.common.tcp.server.TcpServer;

/**
 * 启动程序
 *
 * @author 王超
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = "net.pingfang")
//@Import(value = { VertxTcpClientProvider.class })
@EnableTransactionManagement
public class GateIotApplication implements ApplicationListener<ApplicationReadyEvent> {

	@Resource
	TcpServer tcpServer;

	public static void main(String[] args) {
		// System.setProperty("spring.devtools.restart.enabled", "false");
		SpringApplication.run(GateIotApplication.class, args);
		System.out.println("服务启动成功");
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		AsyncManager.me().execute(tcpServer);
	}

}
