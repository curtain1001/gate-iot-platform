package net.pingfang.flow.subscribe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.websocket.Session;

import org.apache.tomcat.websocket.WsSession;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;

import net.pingfang.common.event.EventBusListener;
import net.pingfang.common.event.EventBusListener.Type;
import net.pingfang.flow.domain.FlowExecuteHistory;
import net.pingfang.flow.event.FlowNodeChangeEvent;
import net.pingfang.flow.service.IFlowExecuteHistoryService;
import net.pingfang.framework.websocket.Message;
import net.pingfang.framework.websocket.SubscribeRequest;
import net.pingfang.framework.websocket.SubscriptionProvider;
import net.pingfang.framework.websocket.WebSocketUsers;

@Component
@EventBusListener(type = Type.ASYNC)
public class FlowRecordSubscriptionProvider implements SubscriptionProvider {

	final IFlowExecuteHistoryService historyService;

	final List<String> sessions = Lists.newCopyOnWriteArrayList();
	final Map<String, Long> mapping = Maps.newConcurrentMap();

	public FlowRecordSubscriptionProvider(IFlowExecuteHistoryService historyService) {
		this.historyService = historyService;
	}

	@Override
	public String id() {
		return "flow-process-record";
	}

	@Override
	public String name() {
		return "流程实例记录";
	}

	@Override
	public String[] getTopicPattern() {
		return new String[] { "/business/flow-process-record/index/*" };
	}

	@Override
	public List<String> getSession() {
		return this.sessions;
	}

	@Override
	public void subscribe(SubscribeRequest request, Session session) {
		Long id = Long.parseLong(request.getTopic().split("[/]")[4]);
		String sessionId = ((WsSession) session).getHttpSessionId();
		sessions.add(sessionId);
		mapping.put(sessionId, id);
	}

	@Override
	public void remove(Session session) {
		String sessionId = ((WsSession) session).getHttpSessionId();
		this.sessions.remove(sessionId);
		this.mapping.remove(sessionId);
	}

//	public Flux<String> send(String id, SubscribeRequest request) {
//		String message = request.getString("request")
//				.orElseThrow(() -> new IllegalArgumentException("参数[request]不能为空"));
//
//		byte[] payload = ByteUtils.string2ByteArr(message);
//
//		return Mono.just((TcpClient) networkManager.<TcpClient>getNetwork(DefaultNetworkType.TCP_CLIENT, id))
//				.flatMap(client -> client.send(new TcpMessage(Unpooled.wrappedBuffer(payload)))).thenReturn("推送成功")
//				.flux();
//	}
//
//	@SuppressWarnings("all")
//	public Flux<String> subscribe(String id, SubscribeRequest request) {
//		String message = request.getString("response").filter(StringUtils::hasText).orElse(null);
//
//		byte[] payload = ByteUtils.string2ByteArr(message);
//
//		return Mono.just((TcpClient) networkManager.<TcpClient>getNetwork(DefaultNetworkType.TCP_CLIENT, id))
//				.flatMapMany(client -> client.subscribe()
//						.flatMap(msg -> client.send(new TcpMessage(Unpooled.wrappedBuffer(payload))).thenReturn(msg))
//						.map(TcpMessage::toString));
//	}

	@Subscribe
	public void on(FlowNodeChangeEvent event) {
		Long instanceId = event.getInstanceId();
		if (mapping.containsValue(instanceId)) {
			List<String> sessionIds = mapping.entrySet().stream().filter(x -> x.getValue().equals(instanceId))
					.map(Entry::getKey).collect(Collectors.toList());
			Set<Session> sessions = WebSocketUsers.getUsers(sessionIds);
			LambdaQueryWrapper<FlowExecuteHistory> historyLambdaQueryWrapper = Wrappers.lambdaQuery();
			historyLambdaQueryWrapper.eq(FlowExecuteHistory::getInstanceId, instanceId);
			historyLambdaQueryWrapper.orderByDesc(FlowExecuteHistory::getCreateTime);
			List<FlowExecuteHistory> histories = historyService.list(historyLambdaQueryWrapper);
			sessions.forEach(session -> {
				WebSocketUsers.sendMessageToUserByText(session,
						Message.success(String.valueOf(instanceId), getTopicPattern()[0], histories));
			});
		}
	}

}
