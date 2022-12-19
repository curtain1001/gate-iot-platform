package net.pingfang.web.core.config;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.tcp.server.TcpServer;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-08 18:41
 */
@Slf4j
@Component
public class CloseServerManager {
	@Resource
	TcpServer tcpServer;

	@PreDestroy
	public void destroy() {
		log.info("---开始关闭服务端---");
		tcpServer.shutdown();
		log.info("---成功关闭服务端---");
	}
}
