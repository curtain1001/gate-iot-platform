package net.pingfang.network.tcp.server;

import java.net.SocketException;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.codec.binary.Hex;

import com.google.common.collect.Maps;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.customizedsetting.values.DefaultCustomized;
import net.pingfang.iot.common.manager.LaneConfigManager;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.parser.PayloadParser;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

/**
 * @author bsetfeng
 * @since 1.0
 **/
@Slf4j
public class VertxTcpServer implements TcpServer {

	@Getter
	private final String id;
	private final EmitterProcessor<NetworkMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<NetworkMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);
	private final ConcurrentMap<String, NetSocket> ipSocket = Maps.newConcurrentMap();
	Collection<NetServer> tcpServers;
	private Supplier<PayloadParser> parserSupplier;
	private final String deviceId;
	private final Long laneId;
	private final LaneConfigManager configManager;

	@Setter
	private long keepAliveTimeout = Duration.ofMinutes(10).toMillis();

	public VertxTcpServer(String id, Long laneId, LaneConfigManager configManager) {
		this.id = id;
		this.deviceId = id; // 设备id = 组件id
		this.laneId = laneId;
		this.configManager = configManager;
	}

	private void execute(Runnable runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			log.warn("close tcp server error", e);
		}
	}

	public void setParserSupplier(Supplier<PayloadParser> parserSupplier) {
		this.parserSupplier = parserSupplier;
	}

	/**
	 * 为每个NetServer添加connectHandler
	 *
	 * @param servers 创建的所有NetServer
	 */
	public void setServer(Collection<NetServer> servers) {
		if (this.tcpServers != null && !this.tcpServers.isEmpty()) {
			shutdown();
		}
		this.tcpServers = servers;
		for (NetServer tcpServer : this.tcpServers) {
			tcpServer.connectHandler(this::acceptTcpConnection);
		}
	}

	@Override
	public Mono<Boolean> send(String ip, TcpMessage message) {
		return Mono.create((sink) -> {
			Buffer buffer = Buffer.buffer(message.getPayload());
			if (StringUtils.isEmpty(ip)) {
				ipSocket.values().forEach(netSocket -> netSocket.write(buffer, r -> {
					if (r.succeeded()) {
						sink.success();
					} else {
						sink.error(r.cause());
					}
				}));
			} else {
				if (ipSocket.get(ip) == null) {
					sink.error(new SocketException("socket closed"));
					return;
				}
				ipSocket.get(ip).write(buffer, r -> {
					if (r.succeeded()) {
						sink.success();
					} else {
						sink.error(r.cause());
					}
				});
			}
		});
	}

	/**
	 * TCP连接处理逻辑
	 *
	 * @param socket socket
	 */
	protected void acceptTcpConnection(NetSocket socket) {
		try {
			// TCP异常和关闭处理
			socket.exceptionHandler(err -> {
				log.error("tcp server client [{}] error", socket.remoteAddress(), err);
			}).closeHandler((nil) -> {
				log.debug("tcp server client [{}] closed", socket.remoteAddress());
			});
			setSocket(socket);
			ipSocket.put(socket.remoteAddress().host(), socket);
			log.debug("accept tcp server [{}] connection", socket.remoteAddress());
		} catch (Exception e) {
			log.error("create tcp server error", e);
		}

	}

	@Override
	public Flux<NetworkMessage> subscribe() {
		return this.processor.map(Function.identity());
	}

	@Override
	public NetworkType getType() {
		return TcpServerNetworkType.TCP_SERVER;
	}

	@Override
	public void shutdown() {
		if (null != tcpServers) {
			for (NetServer tcpServer : tcpServers) {
				execute(tcpServer::close);
			}
			tcpServers = null;
		}
	}

	@Override
	public boolean isAlive() {
		return tcpServers != null;
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	/**
	 * socket处理
	 *
	 * @param socket socket
	 */
	public void setSocket(NetSocket socket) {
		synchronized (this) {
			PayloadParser payloadParser = parserSupplier.get();
			setRecordParser(socket, payloadParser);
			Objects.requireNonNull(parserSupplier.get());
			socket.handler(buffer -> {
				if (log.isDebugEnabled()) {
					log.debug("handle tcp client[{}] payload:[{}]", socket.remoteAddress(),
							Hex.encodeHexString(buffer.getBytes()));
				}
				payloadParser.handle(buffer);
			});
		}
	}

	/**
	 * 设置客户端消息解析器
	 *
	 * @param payloadParser 消息解析器
	 */
	public void setRecordParser(NetSocket socket, PayloadParser payloadParser) {
		synchronized (this) {
			payloadParser.handlePayload().onErrorContinue((err, res) -> {
				log.error(err.getMessage(), err);
			}).subscribe(buffer -> {
				Long laneId = this.laneId;
				if (this.laneId == null && configManager != null) {
					Map<Long, Object> configs = configManager.getConfig(DefaultCustomized.LANE_IP);
					String remoteIp = socket.remoteAddress().host();
					laneId = configs.entrySet().stream().filter(x -> remoteIp.equals(x.getValue())).map(Entry::getKey)
							.findFirst().orElse(null);
				}
				received(NetworkMessage.builder() //
						.deviceId(this.id)//
						.laneId(laneId)//
						.payload(buffer.getBytes())//
						.payloadType(MessagePayloadType.BINARY)//
						.networkType(TcpServerNetworkType.TCP_SERVER)//
						.build());
			});
		}
	}

	/**
	 * 接收TCP消息
	 *
	 * @param message TCP消息
	 */
	protected void received(NetworkMessage message) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn("tcp server [{}],drop message:{}", processor.getPending(), message.toString());
			return;
		}
		sink.next(message);
	}

}
