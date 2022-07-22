package net.pingfang.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.eventbus.EventBus;

/**
 * @author 王超
 * @description TODO
 * @date 2021-11-17 17:46
 */
@Configuration
public class EventBusConfig {
	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}

}
