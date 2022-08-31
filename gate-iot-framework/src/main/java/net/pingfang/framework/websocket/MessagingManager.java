package net.pingfang.framework.websocket;

import javax.websocket.Session;

public interface MessagingManager {

	void subscribe(SubscribeRequest request, Session session);

}
