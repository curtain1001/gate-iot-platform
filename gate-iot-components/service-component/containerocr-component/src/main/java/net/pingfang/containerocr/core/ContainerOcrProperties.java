package net.pingfang.containerocr.core;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 11:31
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ContainerOcrProperties {
	/**
	 * 服务id
	 */
	private String id;
	/**
	 * 服务名称
	 */
	private String name;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * ip
	 */
	private String host;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 报文解析方式
	 */
	private String parserType;

	/**
	 * 报文解析方式
	 */
	private Map<String, Object> parserConfiguration;
	/**
	 * 证书id
	 */
	private String certId;
	/**
	 * 是否开启ssl
	 */

	private boolean ssl;
	/**
	 * 是否保活（长连接）
	 */
	private boolean keepalive;
	/**
	 * 是否自动开启
	 */
	private boolean enabled;
}
