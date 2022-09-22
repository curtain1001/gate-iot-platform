package net.pingfang.network.tcp.client;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.net.NetClientOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.ValueObject;
import net.pingfang.network.tcp.parser.PayloadParserType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TcpClientProperties implements ValueObject {

	private String id;

	private String host;

	private int port;

	private String certId;

	private boolean ssl;

	private PayloadParserType parserType;

	private Map<String, Object> parserConfiguration = new HashMap<>();

	private NetClientOptions options;

	private boolean enabled;

	private boolean KeepAlive;

	@Override
	public Map<String, Object> values() {
		return parserConfiguration;
	}
}
