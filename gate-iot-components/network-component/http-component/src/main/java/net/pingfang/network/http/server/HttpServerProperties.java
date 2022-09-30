package net.pingfang.network.http.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.SocketAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.ValueObject;

/**
 * @author bsetfeng
 * @author zhouhao
 * @since 1.0
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpServerProperties implements ValueObject {

	private String id;

	private Long laneId;

	private HttpServerOptions options;

	private String ContentType;

	private Map<String, Object> parserConfiguration = new HashMap<>();

	private String host;

	private int port;

	private boolean ssl;

	// 服务实例数量(线程数)
	private int instance = Runtime.getRuntime().availableProcessors();

	private String certId;

	public SocketAddress createSocketAddress() {
		if (StringUtils.isEmpty(host)) {
			host = "localhost";
		}
		return SocketAddress.inetSocketAddress(port, host);
	}

	@Override
	public Map<String, Object> values() {
		return parserConfiguration;
	}

}
