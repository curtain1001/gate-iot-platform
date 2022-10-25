package net.pingfang.network.http.server;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.NetworkSession;
import net.pingfang.iot.common.customizedsetting.values.DefaultCustomized;
import net.pingfang.iot.common.manager.LaneConfigManager;
import net.pingfang.iot.common.network.NetworkType;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-28 8:58
 */
@Slf4j
public class VertxHttpServer implements HttpServer, NetworkSession {
	final String id;
	private final EmitterProcessor<NetworkMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<NetworkMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);
	private final String deviceId;
	private final Long laneId;
	Collection<io.vertx.core.http.HttpServer> httpServers;
	private final LaneConfigManager configManager;

	public VertxHttpServer(String id, Long laneId, LaneConfigManager configManager) {
		this.id = id;
		this.deviceId = id;
		this.laneId = laneId;
		this.configManager = configManager;
	}

	@Override
	public Flux<NetworkMessage> subscribe() {
		return this.processor.map(Function.identity());
	}

	private void execute(Runnable runnable) {
		try {
			runnable.run();
		} catch (Exception e) {
			log.warn("close tcp server error", e);
		}
	}

	@Override
	public void shutdown() {
		if (null != httpServers) {
			for (io.vertx.core.http.HttpServer tcpServer : httpServers) {
				execute(tcpServer::close);
			}
			httpServers = null;
		}
	}

	@Override
	public boolean isAlive() {
		return this.httpServers != null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public NetworkType getType() {
		return HttpServerNetworkType.HTTP_SERVER;
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	public void setServer(Collection<io.vertx.core.http.HttpServer> httpServers) {
		if (this.httpServers != null && !this.httpServers.isEmpty()) {
			shutdown();
		}
		this.httpServers = httpServers;
		for (io.vertx.core.http.HttpServer httpServer : this.httpServers) {
			httpServer.requestHandler(this::requestHttpHandler);
		}

	}

	public void requestHttpHandler(HttpServerRequest request) {
		try {
			// HTTP异常和关闭处理
			request.connection().exceptionHandler(err -> {
				log.error("HTTP server client [{}] error", request.remoteAddress(), err);
			}).closeHandler((nil) -> {
				log.debug("HTTP server client [{}] closed", request.remoteAddress());
			});
			log.debug("accept HTTP server [{}] connection", request.remoteAddress());

			Long laneId = this.laneId;
			if (this.laneId == null && configManager != null) {
				Map<Long, Object> configs = configManager.getConfig(DefaultCustomized.LANE_IP);
				String remoteIp = request.remoteAddress().host();
				laneId = configs.entrySet().stream().filter(x -> remoteIp.equals(x.getValue())).map(Entry::getKey)
						.findFirst().orElse(null);
			}

			String contentType = request.getHeader("Content-Type");
			if (StringUtils.isNotEmpty(contentType) && contentType.contains(";")) {
				contentType = contentType.split(";")[0];
			}
			if ("multipart/form-data".equals(contentType)) {
				request.setExpectMultipart(true);
				Long finalLaneId = laneId;
				request.bodyHandler(v -> {
					MultiMap formAttributes = request.formAttributes();
					Map<String, Object> map = formAttributes.entries().stream()
							.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
					String str = JsonUtils.toJsonString(map);
					log.info("http payload:{}", str);
					received(NetworkMessage.builder()//
							.payloadType(MessagePayloadType.JSON)//
							.networkType(HttpServerNetworkType.HTTP_SERVER)//
							.payload(str)//
							.deviceId(deviceId)//
							.laneId(finalLaneId)//
							.build());
				});
			}
			if ("application/x-www-form-urlencoded".equals(contentType)) {
				Long finalLaneId = laneId;
				request.bodyHandler(buffer -> {
					String str = new String(buffer.getBytes(), Charset.forName("GBK"));
					log.info("http payload:{}", str);
					String[] params = str.split("&");
					Map<String, Object> objectMap = Maps.newHashMap();
					Arrays.stream(params).forEach(x -> {
						String[] param = x.split("=");
						objectMap.put(param[0], param[1]);
					});
					received(NetworkMessage.builder()//
							.payloadType(MessagePayloadType.JSON)//
							.networkType(HttpServerNetworkType.HTTP_SERVER)//
							.payload(JsonUtils.toJsonString(objectMap))//
							.deviceId(deviceId)//
							.laneId(finalLaneId)//
							.build());
				});
			}
			if ("application/json".equals(contentType)) {
				Long finalLaneId = laneId;
				request.bodyHandler(buffer -> {
					String str = new String(buffer.getBytes(), StandardCharsets.UTF_8);
					log.info("http payload:{}", str);
					received(NetworkMessage.builder()//
							.payloadType(MessagePayloadType.JSON)//
							.networkType(HttpServerNetworkType.HTTP_SERVER)//
							.payload(str)//
							.deviceId(deviceId)//
							.laneId(finalLaneId)//
							.build());
				});
			}
			request.response().setStatusCode(200).putHeader("content-type", "text/plain")
					.end(JsonUtils.toJsonString(AjaxResult.success("接收成功")));
		} catch (Exception e) {
			log.error("create HTTP server error", e);
		}
	}

	/**
	 * 接收HTTP消息
	 *
	 * @param message HTTP消息
	 */
	protected void received(NetworkMessage message) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn("http server [{}],drop message:{}", processor.getPending(), message.toString());
			return;
		}
		sink.next(message);
	}
}
