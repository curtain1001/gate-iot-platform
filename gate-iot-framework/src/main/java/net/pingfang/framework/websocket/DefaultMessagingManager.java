package net.pingfang.framework.websocket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

//@Component
public class DefaultMessagingManager implements MessagingManager, BeanPostProcessor {

	private final Map<String, SubscriptionProvider> subProvider = new ConcurrentHashMap<>();

	private final static PathMatcher matcher = new AntPathMatcher();

	@Override
	public void subscribe(SubscribeRequest request, Session session) {
		for (Map.Entry<String, SubscriptionProvider> entry : subProvider.entrySet()) {
			if (matcher.match(entry.getKey(), request.getTopic())) {
				entry.getValue().subscribe(request, session);
			}
		}
	}

	public void register(SubscriptionProvider provider) {
		for (String pattern : provider.getTopicPattern()) {
			subProvider.put(pattern, provider);
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof SubscriptionProvider) {
			register(((SubscriptionProvider) bean));
		}
		return bean;
	}
}
