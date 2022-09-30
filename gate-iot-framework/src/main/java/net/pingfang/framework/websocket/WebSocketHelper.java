package net.pingfang.framework.websocket;

import javax.annotation.Resource;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-30 14:20
 */
@Component
@Slf4j
public class WebSocketHelper {

	@Resource
	private SimpMessagingTemplate messagingTemplate;

	/**
	 * 通过websocket点对点发送单一定阅用户
	 *
	 * @param subsAdd 用户的定阅地址，不需要拼接前缀和用户id
	 * @param msg     发送的内容，Json字符串格式
	 * @param userId  userId,需要发送的用户SaasCode
	 */
	public void sendToUser(String subsAdd, Object msg, String userId) {
		messagingTemplate.convertAndSendToUser(userId, subsAdd, msg);
		log.info("Send to user {} through webSocket successful!", userId);
	}

	/**
	 * 通过websocket点对点发送多个定阅用户
	 *
	 * @param subsAdd 用户的定阅地址，不需要拼接前缀和用户id
	 * @param msg     发送的内容，Json字符串格式
	 * @param userIds 需要发送的用户id数组
	 */
	public void sendToUsers(String subsAdd, Object msg, String[] userIds) {
		if (userIds != null && userIds.length > 0) {
			for (String userId : userIds) {
				sendToUser(subsAdd, msg, userId);
			}
		}
	}

	/**
	 * 通过websocket广播消息,发给所有定阅用户
	 *
	 * @param subsAdd 用户的定阅地址
	 * @param msg     发送的内容，Json字符串格式
	 */
	public void broadCast(String subsAdd, Object msg) {
		messagingTemplate.convertAndSend(subsAdd, msg);
		log.info("BroadCast through webSocket successfully!");
	}
}
