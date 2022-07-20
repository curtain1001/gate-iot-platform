package net.pingfang.framework.socket;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagingRequest {

	private String id;

	private Type type;

	private String topic;

	private Map<String, Object> parameter;

	public enum Type {
		pub, sub, unsub, ping
	}
}
