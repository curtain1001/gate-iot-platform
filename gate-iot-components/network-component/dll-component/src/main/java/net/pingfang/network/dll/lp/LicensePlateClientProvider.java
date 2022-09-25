//package net.pingfang.network.dll.lp;
//
//import java.lang.reflect.InvocationTargetException;
//import java.time.Duration;
//import java.util.List;
//
//import javax.annotation.Nonnull;
//
//import org.springframework.stereotype.Component;
//
//import io.vertx.core.Vertx;
//import io.vertx.core.net.NetClient;
//import io.vertx.core.net.NetClientOptions;
//import net.pingfang.common.utils.bean.BeanUtils;
//import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
//import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
//import net.pingfang.iot.common.network.NetworkType;
//import net.pingfang.network.DefaultNetworkType;
//import net.pingfang.network.Network;
//import net.pingfang.network.NetworkProperties;
//import net.pingfang.network.NetworkProvider;
//import net.pingfang.network.dll.lp.config.SdkInit;
//import net.pingfang.network.security.CertificateManager;
//import net.sdk.function.main.NET;
//
///**
// * <p>
// *
// * </p>
// *
// * @author 王超
// * @since 2022-09-24 15:37
// */
//@Component
//public class LicensePlateClientProvider implements NetworkProvider<LicensePlateClientProperties> {
//	final NET net;
//
//	public LicensePlateClientProvider() {
//		SdkInit.init();
//		this.net = SdkInit.net;
//	}
//
//	@Override
//	public NetworkType getType() {
//		return DefaultNetworkType.LP_DLL;
//	}
//
//	@Override
//	public LicensePlateClient createNetwork(LicensePlateClientProperties properties) {
//		LicensePlateClient client = new LicensePlateClient();
//		VertxTcpClient client = new VertxTcpClient(properties.getId(), true);
//		initClient(client, properties);
//		return client;
//		return null;
//	}
//
//	@Override
//	public void reload(Network network, LicensePlateClientProperties properties) {
//
//	}
//
//	@Override
//	public LicensePlateClientProperties createConfig(NetworkProperties properties)
//			throws InvocationTargetException, IllegalAccessException {
//		LicensePlateClientProperties config = new LicensePlateClientProperties();
//		BeanUtils.copyBean(config, properties.getConfigurations());
//		config.setId(properties.getId());
//		return config;
//	}
//
//	@Override
//	public List<CustomizedSettingData> getBasicForm() {
//		return null;
//	}}
//
//	private final CertificateManager certificateManager;
//
//	private final PayloadParserBuilder payloadParserBuilder;
//
//	private final Vertx vertx;
//
//	public VertxTcpClientProvider(CertificateManager certificateManager, Vertx vertx,
//			PayloadParserBuilder payloadParserBuilder) {
//		this.certificateManager = certificateManager;
//		this.vertx = vertx;
//		this.payloadParserBuilder = payloadParserBuilder;
//	}
//
//	@Nonnull
//	@Override
//	public VertxTcpClient createNetwork(@Nonnull TcpClientProperties properties) {
//		VertxTcpClient client = new VertxTcpClient(properties.getId(), true);
//		initClient(client, properties);
//		return client;
//	}
//
//	@Override
//	public void reload(@Nonnull Network network, @Nonnull TcpClientProperties properties) {
//		initClient(((VertxTcpClient) network), properties);
//	}
//
//	public void initClient(VertxTcpClient client, TcpClientProperties properties) {
//		NetClientOptions options = properties.getOptions();
//		options.setReconnectAttempts(10);
//		options.setReconnectInterval(500);
//		options.setTcpKeepAlive(properties.isKeepAlive());
//		NetClient netClient = vertx.createNetClient(options);
//		client.setClient(netClient);
//		client.setKeepAliveTimeoutMs(properties.getLong("keepAliveTimeout").orElse(Duration.ofMinutes(10).toMillis()));
//		netClient.connect(properties.getPort(), properties.getHost(), result -> {
//			if (result.succeeded()) {
//				log.debug("connect net.pingfang.gateiot.network.tcp [{}:{}] success", properties.getHost(),
//						properties.getPort());
//				client.setRecordParser(payloadParserBuilder.build(properties.getParserType(), properties));
//				client.setSocket(result.result());
//			} else {
//				log.error("connect net.pingfang.gateiot.network.tcp [{}:{}] error", properties.getHost(),
//						properties.getPort(), result.cause());
//			}
//		});
//	}
//
//	@Override
//	public List<CustomizedSettingData> getBasicForm() {
//		return CustomizedSettingRepository.getValues(TcpClientBasicFormCustomized.values());
//	}
