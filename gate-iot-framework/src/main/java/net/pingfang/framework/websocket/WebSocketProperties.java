package net.pingfang.framework.websocket;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
 * @since 2022-09-30 14:21
 */
@Data
@Builder(toBuilder = true)
@ConfigurationProperties(prefix = "websocket")
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketProperties {
	/**
	 * 广播端点
	 */
	String topicEndPoint;
	/**
	 * 点对点端点
	 */
	String userEndPoint;
}
