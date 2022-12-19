package net.pingfang.framework.websocket;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.AjaxResult;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-30 14:17
 */
@Controller
@Slf4j
public class RestWebSocketController {

	/**
	 * 发起一条广播消息
	 *
	 * @param message   接收的消息体
	 * @return 广播发送的消息
	 */
	@MessageMapping("/res/device-status")
	public AjaxResult topic(String message) {
		log.info("来自的消息{}", message);
		return AjaxResult.success("Hello,Spring,这是客户端主动拉取的topic消息!");
	}

//	@SubscribeMapping("/topic/public/device-status")
//	public AjaxResult sub(@Headers Map<String, Object> map) {
//		return AjaxResult.success("成功");
//	}

	@MessageExceptionHandler
	public void handleExceptions(Throwable t) {
		log.error("Error handling message: " + t.getMessage());
	}
//
//	/**
//	 * 发送消息到当前请求用户
//	 *
//	 * @param principal 身份标识
//	 * @param message   接收的消息体
//	 * @return 发送到当前请求用户的消息
//	 */
//	@MessageMapping({ "/user" })
//	@SendToUser(value = "/user/ping", broadcast = false)
//	public AjaxResult queue(Principal principal, String message) {
//		log.info("来自{}的消息{}", principal.getName(), message);
//		return AjaxResult.success("Hello," + principal.getName() + ",这是客户端主动拉取的topic消息!");
//	}
}
