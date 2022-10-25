package net.pingfang.network.http.server;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.bean.BeanUtils;
import net.pingfang.iot.common.manager.LaneConfigManager;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.Network;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkProvider;
import net.pingfang.network.security.Certificate;
import net.pingfang.network.security.CertificateManager;
import net.pingfang.network.security.VertxKeyCertTrustOptions;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-28 8:54
 */
@Component
@Slf4j
public class VertxHttpProvider implements NetworkProvider<HttpServerProperties> {
	private final Vertx vertx;
	private final CertificateManager certificateManager;
	private final LaneConfigManager configManager;

	public VertxHttpProvider(Vertx vertx, CertificateManager certificateManager, LaneConfigManager configManager) {
		this.vertx = vertx;
		this.certificateManager = certificateManager;
		this.configManager = configManager;
	}

	@Override
	public NetworkType getType() {
		return HttpServerNetworkType.HTTP_SERVER;
	}

	@Override
	public Network createNetwork(HttpServerProperties properties) {
		VertxHttpServer httpServer = new VertxHttpServer(properties.getId(), properties.getLaneId(), configManager);
		initHttpServer(httpServer, properties);
		return httpServer;
	}

	/**
	 * HTTP服务初始化
	 *
	 * @param httpServer HTTP服务
	 * @param properties HTTP配置
	 */
	private void initHttpServer(VertxHttpServer httpServer, HttpServerProperties properties) {
		int instance = Math.max(2, properties.getInstance());
		List<HttpServer> instances = new ArrayList<>(instance);
		for (int i = 0; i < instance; i++) {
			instances.add(vertx.createHttpServer(properties.getOptions()));
		}
		// 根据解析类型配置数据解析器
		httpServer.setServer(instances);
//        httpServer.setKeepAliveTimeout(properties.getLong("keepAliveTimeout", Duration.ofMinutes(10).toMillis()));
		// 针对JVM做的多路复用优化
		// 多个server listen同一个端口，每个client连接的时候vertx会分配
		// 一个connection只能在一个server中处理
		for (HttpServer server : instances) {

			server.listen(properties.createSocketAddress(), result -> {
				if (result.succeeded()) {
					log.info("tcp server startup on {}", result.result().actualPort());
				} else {
					log.error("tcp server error", result.cause());
				}
			});
		}
	}

	@Override
	public void reload(Network network, HttpServerProperties properties) {
		VertxHttpServer httpServer = ((VertxHttpServer) network);
		httpServer.shutdown();
		initHttpServer(httpServer, properties);
	}

	@Override
	public HttpServerProperties createConfig(NetworkProperties properties)
			throws InvocationTargetException, IllegalAccessException {
		HttpServerProperties config = new HttpServerProperties();
//		ConvertUtils.register(new Converter() {
//			@Override
//			public Object convert(Class type, Object value) {
//				return PayloadParserType.valueOf(value.toString());
//			}
//		}, PayloadParserType.class);
		BeanUtils.copyBean(config, properties.getConfigurations());
		config.setId(properties.getId());
		if (config.getOptions() == null) {
			config.setOptions(new HttpServerOptions());
		}
		if (config.isSsl()) {
			config.getOptions().setSsl(true);
			Certificate certificate = certificateManager.getCertificate(config.getCertId());
			VertxKeyCertTrustOptions vertxKeyCertTrustOptions = new VertxKeyCertTrustOptions(certificate);
			config.getOptions().setKeyCertOptions(vertxKeyCertTrustOptions);
			config.getOptions().setTrustOptions(vertxKeyCertTrustOptions);
		}
		return config;
	}

}
