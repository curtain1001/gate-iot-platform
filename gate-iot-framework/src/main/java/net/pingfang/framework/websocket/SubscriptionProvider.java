package net.pingfang.framework.websocket;

import java.util.List;

import javax.websocket.Session;

public interface SubscriptionProvider {

	String id();

	String name();

	String[] getTopicPattern();

	public List<String> getSession();

	void subscribe(SubscribeRequest request, Session session);

	void remove(Session session);

}
