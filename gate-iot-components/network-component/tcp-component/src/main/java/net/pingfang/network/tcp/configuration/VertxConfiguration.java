package net.pingfang.network.tcp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:30
 */
@Configuration
public class VertxConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "vertx")
	public VertxOptions vertxOptions() {
		return new VertxOptions();
	}

	@Bean
	public Vertx vertx(VertxOptions vertxOptions) {
		return Vertx.vertx(vertxOptions);
	}

	@Bean
	public WorkerExecutor workerExecutor(Vertx vertx) {
		return vertx.createSharedWorkerExecutor("worker-pool");
	}

}
