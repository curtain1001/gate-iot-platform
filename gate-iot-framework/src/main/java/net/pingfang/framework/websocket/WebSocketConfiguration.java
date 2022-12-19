package net.pingfang.framework.websocket;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import lombok.extern.slf4j.Slf4j;
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
	private TokenService tokenService;
	@Resource
	WebSocketProperties webSocketProperties;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 客户端订阅消息的前缀：topic用来广播，user用来实现点对点
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 用于广播的endPoint
		registry.addEndpoint(webSocketProperties.getTopicEndPoint()) //
//				.addInterceptors(interceptor) //
//				.setHandshakeHandler(handshakeHandler) //
				.setAllowedOriginPatterns("*") //
				.withSockJS() //
				.setMessageCodec(new Jackson2SockJsMessageCodec());
	}

	/*
	 * 1. setMessageSizeLimit 设置消息缓存的字节数大小 字节 2. setSendBufferSizeLimit
	 * 设置websocket会话时，缓存的大小 字节 3. setSendTimeLimit 设置消息发送会话超时时间，毫秒
	 */
	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

		registry.setSendTimeLimit(15 * 1000)
				.setSendBufferSizeLimit(512 * 1024)
				.setMessageSizeLimit(64 * 1024);
	}

//	private final HandshakeInterceptor interceptor = new HandshakeInterceptor() {
//		@Override
//		public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
//				WebSocketHandler wsHandler, Map<String, Object> attributes) {
//			log.info("--websocket的http连接握手之前--");
//			try {
//				ServletServerHttpRequest req = (ServletServerHttpRequest) request;
//				String token = req.getServletRequest().getParameter("Authorization");
//				log.info("Authorization:" + token);
//				LoginUser loginUser = tokenService.getLoginUser(req.getServletRequest());
//				if (loginUser == null) {
//					log.error("token is invalid, webSocket connect can`t be establish");
//					return false;
//				}
//				attributes.put("userId", loginUser.getUserId());
//			} catch (Exception e) {
//				log.error("权限验证失败：", e);
//				return false;
//			}
//			return true;
//		}
//
//		@Override
//		public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//				Exception exception) {
//			log.error(exception.getMessage());
//		}
//	};
//
//	private DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler() {
//		@Override
//		protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
//				Map<String, Object> attributes) {
//			return new UserPrincipal((String) attributes.get("userId"));
//		}
//	};
}
