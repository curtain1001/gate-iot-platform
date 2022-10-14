package net.pingfang.framework.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import java.security.Principal;
import java.util.Map;

import javax.annotation.Resource;

import net.pingfang.common.utils.JsonUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.sun.security.auth.UserPrincipal;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.model.LoginUser;
import net.pingfang.framework.web.service.TokenService;

/**
 * websocket 配置
 *
 * @author ruoyi
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(WebSocketProperties.class)
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {
	@Resource
	private WebSocketProperties webSocketProperties;

	@Resource
	private TokenService tokenService;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 客户端订阅消息的前缀：topic用来广播，user用来实现点对点
		registry.enableSimpleBroker("/topic", "/user");
		// 点对点发送前缀
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 用于广播的endPoint
		registry.addEndpoint(webSocketProperties.getTopicEndPoint()) //
				.addInterceptors(interceptor) //
				.setHandshakeHandler(handshakeHandler) //
				.setAllowedOriginPatterns("*") //
				.withSockJS(); //
		// 用于点对点通信的endPoint
		registry.addEndpoint(webSocketProperties.getUserEndPoint()) //
				.addInterceptors(interceptor) //
				.setHandshakeHandler(handshakeHandler) //
				.setAllowedOriginPatterns("*") //
				.withSockJS();
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		// The default value is 10 seconds (i.e. 10 * 10000).
		registry.setSendTimeLimit(15 * 1000)
				// the default value is 512K (i.e. 512 * 1024).
				.setSendBufferSizeLimit(512 * 1024)
				// The default value is 64K (i.e. 64 * 1024).
				.setMessageSizeLimit(64 * 1024);
	}

	private HandshakeInterceptor interceptor = new HandshakeInterceptor() {
		@Override
		public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
				WebSocketHandler wsHandler, Map<String, Object> attributes) {
//			try {
//				LoginUser loginUser = tokenService
//						.getLoginUser(((ServletServerHttpRequest) request).getServletRequest());
//				if (loginUser == null) {
//					log.error("token is invalid, webSocket connect can`t be establish");
//					return false;
//				}
//				attributes.put("userId", loginUser.getUserId());
//			} catch (Exception e) {
//				log.error("权限验证失败：", e);
//				return false;
//			}

			return true;
		}

		@Override
		public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
				Exception exception) {
		}
	};

	private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler() {
		@Override
		protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
				Map<String, Object> attributes) {
			return new UserPrincipal((String) attributes.get("userId"));
		}
	};
}
