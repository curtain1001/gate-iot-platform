package net.pingfang.business.manager;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import net.pingfang.common.event.EventBusListener;
import net.pingfang.common.utils.uuid.UUID;
import net.pingfang.iot.common.manager.GateFLowManager;
import net.pingfang.services.events.EndGateFlowEvent;
import net.pingfang.services.events.StartGateFlowEvent;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 22:58
 */
@Component
@EventBusListener
public class DefaultGateFLowManager implements GateFLowManager {
	final Map<Long, String> gateFlow = Maps.newConcurrentMap();

	@Override
	public String getFlowId(long laneId) {
		return gateFlow.get(laneId);
	}

	public void remove(long laneId) {
		gateFlow.remove(laneId);
	}

	/**
	 * 开始流程
	 */
	@Subscribe
	public void on(StartGateFlowEvent event) {
		String flowId = UUID.randomUUID().toString(true);
		gateFlow.put(event.getLaneId(), flowId);
	}

	/**
	 * 结束流程
	 *
	 */
	@Subscribe
	public void on(EndGateFlowEvent event) {
		remove(event.getLaneId());
	}

}
