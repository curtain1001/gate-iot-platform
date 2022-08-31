//package net.pingfang.framework.socket;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.web.reactive.HandlerMapping;
//import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
//import org.springframework.web.reactive.socket.WebSocketHandler;
//import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
//
//import net.pingfang.framework.web.service.TokenService;
//import net.pingfang.framework.websocket.MessagingManager;
//
////@Configuration
//public class WebSocketMessagingHandlerConfiguration {
//
//	@Bean
//	public HandlerMapping webSocketMessagingHandlerMapping(MessagingManager messagingManager,
//			TokenService tokenService) {
//
//		WebSocketMessagingHandler messagingHandler = new WebSocketMessagingHandler(messagingManager, tokenService);
//		final Map<String, WebSocketHandler> map = new HashMap<>(1);
//		map.put("/websocket/**", messagingHandler);
//
//		final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
//		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		mapping.setUrlMap(map);
//		return mapping;
//	}
//
//	@Bean
//	@ConditionalOnMissingBean
//	public WebSocketHandlerAdapter webSocketHandlerAdapter() {
//		return new WebSocketHandlerAdapter();
//	}
//
//}
