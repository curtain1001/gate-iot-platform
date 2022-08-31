package net.pingfang.containerocr.core;

import javax.annotation.Resource;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.common.event.EventBusListener;
import net.pingfang.common.event.EventBusListener.Type;
import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.server.TcpServer;
import net.pingfang.servicecomponent.core.SupportServerManager;
import net.pingfang.servicecomponent.event.SupportServerEvent;
import net.pingfang.servicecomponent.values.SupportConfigure;
import reactor.core.Disposable;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 11:52
 */
@Component
@Slf4j
@EventBusListener(type = Type.ASYNC)
public class ContainerOcr implements ApplicationRunner {
	@Resource
	NetworkManager networkManager;
	@Resource
	SupportServerManager supportServerManager;
	@Resource
	EventBusCenter eventBusCenter;

	private Disposable disposable;

	@Subscribe
	public void handler(SupportServerEvent event) {
		if (event.getProduct().equals(ContainerOcrProduct.CONTAINER_OCR.getValue())) {
			SupportConfigure configure = supportServerManager
					.getConfigure(ContainerOcrProduct.CONTAINER_OCR.getValue());
			if (configure != null) {
				TcpServer tcpServer = (TcpServer) networkManager.getNetwork(DefaultNetworkType.TCP_SERVER,
						configure.getNetworkId());
				if (tcpServer != null) {
					if (disposable != null) {
						disposable.dispose();
					}
					tcpServer.handleConnection().flatMap(TcpClient::subscribe).map(TcpMessage::getPayload)
							.subscribe(x -> {
								log.info("箱号识别订阅:{}", x);
							});

				}
			}
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

	}
}
