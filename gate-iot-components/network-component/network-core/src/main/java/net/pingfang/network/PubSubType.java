package net.pingfang.network;

import org.hswebframework.web.dict.EnumDict;

import lombok.Getter;

@Getter
public enum PubSubType implements EnumDict<String> {

	producer, consumer;

	@Override
	public String getValue() {
		return name();
	}

	@Override
	public String getText() {
		return name();
	}
}
