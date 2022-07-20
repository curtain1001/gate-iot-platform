package net.pingfang.framework.socket;

import reactor.core.publisher.Flux;

public interface MessagingManager {

	Flux<Message> subscribe(SubscribeRequest request);

}
