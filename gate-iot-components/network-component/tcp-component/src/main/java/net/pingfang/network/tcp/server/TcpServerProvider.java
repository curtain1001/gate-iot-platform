package net.pingfang.network.tcp.server;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.bean.BeanUtils;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.Network;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkProvider;
import net.pingfang.network.NetworkType;
import net.pingfang.network.security.Certificate;
import net.pingfang.network.security.CertificateManager;
import net.pingfang.network.security.VertxKeyCertTrustOptions;
import net.pingfang.network.tcp.parser.PayloadParserBuilder;

/**
 * TCP服务提供商
 *
 * @author zhouhao
 */
@Component
@Slf4j
public class TcpServerProvider implements NetworkProvider<TcpServerProperties> {

	private final CertificateManager certificateManager;

	private final Vertx vertx;

	private final PayloadParserBuilder payloadParserBuilder;

	public TcpServerProvider(CertificateManager certificateManager, Vertx vertx,
			PayloadParserBuilder payloadParserBuilder) {
		this.certificateManager = certificateManager;
		this.vertx = vertx;
		this.payloadParserBuilder = payloadParserBuilder;
	}

	@Nonnull
	@Override
	public NetworkType getType() {
		return DefaultNetworkType.TCP_SERVER;
	}

	@Nonnull
	@Override
	public VertxTcpServer createNetwork(@Nonnull TcpServerProperties properties) {

		VertxTcpServer tcpServer = new VertxTcpServer(properties.getId());
		initTcpServer(tcpServer, properties);

		return tcpServer;
	}

	/**
	 * TCP服务初始化
	 *
	 * @param tcpServer  TCP服务
	 * @param properties TCP配置
	 */
	private void initTcpServer(VertxTcpServer tcpServer, TcpServerProperties properties) {
		int instance = Math.max(2, properties.getInstance());
		List<NetServer> instances = new ArrayList<>(instance);
		for (int i = 0; i < instance; i++) {
			instances.add(vertx.createNetServer(properties.getOptions()));
		}
		// 根据解析类型配置数据解析器
		payloadParserBuilder.build(properties.getParserType(), properties);
		tcpServer.setParserSupplier(() -> payloadParserBuilder.build(properties.getParserType(), properties));
		tcpServer.setServer(instances);
		tcpServer.setKeepAliveTimeout(properties.getLong("keepAliveTimeout", Duration.ofMinutes(10).toMillis()));
		// 针对JVM做的多路复用优化
		// 多个server listen同一个端口，每个client连接的时候vertx会分配
		// 一个connection只能在一个server中处理
		for (NetServer netServer : instances) {
			netServer.listen(properties.createSocketAddress(), result -> {
				if (result.succeeded()) {
					log.info("net.pingfang.gateiot.network.tcp server startup on {}", result.result().actualPort());
				} else {
					log.error("startup net.pingfang.gateiot.network.tcp server error", result.cause());
				}
			});
		}
	}

	@Override
	public void reload(@Nonnull Network network, @Nonnull TcpServerProperties properties) {
		VertxTcpServer tcpServer = ((VertxTcpServer) network);
		tcpServer.shutdown();
		initTcpServer(tcpServer, properties);
	}

	@Nonnull
	@Override
	public TcpServerProperties createConfig(@Nonnull NetworkProperties properties) {
		TcpServerProperties config = new TcpServerProperties();
		BeanUtils.copyBeanProp(config, properties.getConfigurations());
		config.setId(properties.getId());
		if (config.getOptions() == null) {
			config.setOptions(new NetServerOptions());
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
