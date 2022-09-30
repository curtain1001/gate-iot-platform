package net.pingfang.network.tcp.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

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
		Vertx vertx = Vertx.vertx(vertxOptions);
		final String JAVASCRIPT_CLIENT_ADDRESS = "eventbus.ui.javascript";
		final String WEBSOCKET_SERVER_ADDRESS = "websocket_server_address";
		final String GO_CLIENT_ADDRESS = "eventbus.test.go";
		final String PYTHON_CLIENT_ADDRESS = "eventbus.test.python";
		final String NODEJS_CLIENT_ADDRESS = "eventbus.test.nodejs";
		final String SOCKET_SERVER_ADDRESS = "websocket_server_address";

		Router router = Router.router(vertx);
		EventBus eb = vertx.eventBus();
		SockJSBridgeOptions options = new SockJSBridgeOptions()
				.addInboundPermitted(new PermittedOptions().setAddress(GO_CLIENT_ADDRESS))
				.addInboundPermitted(new PermittedOptions().setAddress(NODEJS_CLIENT_ADDRESS))
				.addInboundPermitted(new PermittedOptions().setAddress(PYTHON_CLIENT_ADDRESS))
				.addOutboundPermitted(new PermittedOptions().setAddress(SOCKET_SERVER_ADDRESS))
				.addOutboundPermitted(new PermittedOptions().setAddressRegex(JAVASCRIPT_CLIENT_ADDRESS))
				.addInboundPermitted(new PermittedOptions().setAddressRegex((JAVASCRIPT_CLIENT_ADDRESS)));

		SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
		router.mountSubRouter("/", sockJSHandler.bridge(options));

		vertx.createHttpServer().requestHandler(router).listen(8999);

		eb.consumer(WEBSOCKET_SERVER_ADDRESS, l -> {
			eb.send(JAVASCRIPT_CLIENT_ADDRESS, l.body());
		});
		eb.consumer(GO_CLIENT_ADDRESS, l -> {
			eb.send(JAVASCRIPT_CLIENT_ADDRESS, l.body());
		});
		return vertx;
	}

	@Bean
	public WorkerExecutor workerExecutor(Vertx vertx) {
		return vertx.createSharedWorkerExecutor("worker-pool");
	}

}
