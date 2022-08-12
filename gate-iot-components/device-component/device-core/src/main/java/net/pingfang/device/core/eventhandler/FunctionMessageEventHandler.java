package net.pingfang.device.core.eventhandler;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.event.MessageUpEvent;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-21 16:09
 */
@Component
@Slf4j
public class FunctionMessageEventHandler {

	@Subscribe
	public void on(MessageUpEvent event) {
		log.info("设备：{}；指令：{}；", event.getDeviceId(),
				event.getInstruction() != null ? event.getInstruction().getValue() : "");
	}

}
