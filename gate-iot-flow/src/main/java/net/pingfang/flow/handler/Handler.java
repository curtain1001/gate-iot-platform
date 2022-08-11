package net.pingfang.flow.handler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.EventBus;

import net.pingfang.flow.core.FlowEngine;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-21 18:01
 */
@Component
public class Handler {
	@Resource
	EventBus eventBus;

	@Resource
	FlowEngine engine;

	@PostConstruct
	public void on() {
		eventBus.register(engine);
	}
}
