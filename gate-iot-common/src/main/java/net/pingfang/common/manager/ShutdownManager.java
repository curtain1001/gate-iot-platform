package net.pingfang.common.manager;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.pingfang.common.tcp.server.TcpServer;

/**
 * 确保应用退出时能关闭后台线程
 *
 * @author ruoyi
 */
@Component
public class ShutdownManager {
	private static final Logger logger = LoggerFactory.getLogger("sys-user");

	@Resource
	TcpServer tcpServer;

	@PreDestroy
	public void destroy() {
		logger.info("====关闭TCP服务====");
		tcpServer.shutdown();
		logger.info("====成功关闭TCP服务====");
		shutdownAsyncManager();
	}

	/**
	 * 停止异步执行任务
	 */
	private void shutdownAsyncManager() {
		try {
			logger.info("====关闭后台任务任务线程池====");
			AsyncManager.me().shutdown();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
