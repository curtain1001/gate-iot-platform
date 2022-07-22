package net.pingfang.device.core.handler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import net.pingfang.device.core.eventhandler.FunctionMessageEventHandler;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-21 18:01
 */
@Component
public class RegisterEventHandler {
	@Resource
	EventBus eventBus;
	@Resource
	FunctionMessageEventHandler eventHandler;

	@PostConstruct
	public void on() {
		eventBus.register(eventHandler);
	}
}
