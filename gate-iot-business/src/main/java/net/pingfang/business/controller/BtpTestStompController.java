package net.pingfang.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pingfang.business.values.Topic;
import net.pingfang.common.core.domain.AjaxResult;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-18 15:11
 */
@RestController
@RequestMapping("stomp")
public class BtpTestStompController {
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@PostMapping
	public AjaxResult test(@RequestBody Topic topic) {
		messagingTemplate.convertAndSend("/topic/public/device-status", "sdfasdfasd");
		return AjaxResult.success();
	}
}
