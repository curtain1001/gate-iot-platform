package net.pingfang.framework.config;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

/**
 * @author 王超
 * @description TODO
 * @date 2021-11-17 17:46
 */
@Component
public class EventBusCenter {
	private static final EventBus eventBus = new EventBus();

	private EventBusCenter() {
	}

	public static EventBus getInstance() {
		return eventBus;
	}

	public static void register(Object object) {
		eventBus.register(object);
	}

	public static void unregister(Object object) {
		eventBus.unregister(object);
	}

	public static void post(Object object) {
		eventBus.post(object);
	}
}
