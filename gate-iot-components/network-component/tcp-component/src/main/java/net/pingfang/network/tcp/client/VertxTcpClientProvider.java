package net.pingfang.network.tcp.client;

import java.time.Duration;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
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

@Component
@Slf4j
public class VertxTcpClientProvider implements NetworkProvider<TcpClientProperties> {

	private final CertificateManager certificateManager;

	private final PayloadParserBuilder payloadParserBuilder;

	private final Vertx vertx;

	public VertxTcpClientProvider(CertificateManager certificateManager, Vertx vertx,
			PayloadParserBuilder payloadParserBuilder) {
		this.certificateManager = certificateManager;
		this.vertx = vertx;
		this.payloadParserBuilder = payloadParserBuilder;
	}

	@Nonnull
	@Override
	public NetworkType getType() {
		return DefaultNetworkType.TCP_CLIENT;
	}

	@Nonnull
	@Override
	public VertxTcpClient createNetwork(@Nonnull TcpClientProperties properties) {
		VertxTcpClient client = new VertxTcpClient(properties.getId(), false);

		initClient(client, properties);

		return client;
	}

	@Override
	public void reload(@Nonnull Network network, @Nonnull TcpClientProperties properties) {
		initClient(((VertxTcpClient) network), properties);
	}

	public void initClient(VertxTcpClient client, TcpClientProperties properties) {
		NetClientOptions options = properties.getOptions();
		options.setReconnectAttempts(10);
		options.setReconnectInterval(500);
		NetClient netClient = vertx.createNetClient(properties.getOptions());
		client.setClient(netClient);
		client.setKeepAliveTimeoutMs(properties.getLong("keepAliveTimeout").orElse(Duration.ofMinutes(10).toMillis()));
		netClient.connect(properties.getPort(), properties.getHost(), result -> {
			if (result.succeeded()) {
				log.debug("connect net.pingfang.gateiot.network.tcp [{}:{}] success", properties.getHost(),
						properties.getPort());
				client.setRecordParser(payloadParserBuilder.build(properties.getParserType(), properties));
				client.setSocket(result.result());
			} else {
				log.error("connect net.pingfang.gateiot.network.tcp [{}:{}] error", properties.getHost(),
						properties.getPort(), result.cause());
			}
		});
	}

	@Override
	public TcpClientProperties createConfig(NetworkProperties properties) {
		TcpClientProperties config = new TcpClientProperties();
		BeanUtils.copyBeanProp(config, properties.getConfigurations());
		config.setId(properties.getId());
		if (config.getOptions() == null) {
			config.setOptions(new NetClientOptions());
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
