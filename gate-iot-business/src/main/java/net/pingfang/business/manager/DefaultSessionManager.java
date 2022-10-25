package net.pingfang.business.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.NetworkSession;
import net.pingfang.iot.common.manager.SessionManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-27 9:28
 */
@Component
@Slf4j
public class DefaultSessionManager implements SessionManager {
	private final Map<String, NetworkSession> store = new ConcurrentHashMap<>();
//	private final DefaultInstructionManager instructionManager;

//	public DefaultSessionManager(DefaultInstructionManager instructionManager) {
////		this.instructionManager = instructionManager;
//	}

	@Override
	public void register(String id, NetworkSession session) {
		store.put(id, session);
		session.subscribe().subscribe(x -> {
			log.info("车道：{}；设备号：{}；网络组件消息：{}", x.getLaneId(), x.getDeviceId(), x.payloadAsString());
//			instructionManager.receiveMessage(x);
		});
	}

	@Override
	public void shutdown(String id) {
		store.remove(id);
	}
}
