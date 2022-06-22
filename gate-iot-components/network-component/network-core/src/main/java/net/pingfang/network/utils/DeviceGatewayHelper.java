//package net.pingfang.network.utils;
//
//import java.time.Duration;
//import java.util.function.Consumer;
//import java.util.function.Function;
//import java.util.function.Supplier;
//
//import org.jetlinks.community.PropertyConstants;
//import org.jetlinks.core.device.DeviceConfigKey;
//import org.jetlinks.core.device.DeviceOperator;
//import org.jetlinks.core.device.DeviceRegistry;
//import org.jetlinks.core.message.*;
//import org.jetlinks.core.message.state.DeviceStateCheckMessage;
//import org.jetlinks.core.message.state.DeviceStateCheckMessageReply;
//import org.jetlinks.core.server.session.ChildrenDeviceSession;
//import org.jetlinks.core.server.session.DeviceSession;
//import org.jetlinks.core.server.session.DeviceSessionManager;
//import org.jetlinks.core.server.session.KeepOnlineSession;
//import org.jetlinks.supports.server.DecodedClientMessageHandler;
//import org.springframework.util.StringUtils;
//
//import lombok.AllArgsConstructor;
//import reactor.core.publisher.Mono;
//
///**
// * 设备网关处理工具
// * <p>
// * 封装常用的设备消息处理操作
// * </p>
// *
// * @author zhouhao
// */
//@AllArgsConstructor
//public class DeviceGatewayHelper {
//
//	private final DeviceRegistry registry;
//	private final DeviceSessionManager sessionManager;
//	private final DecodedClientMessageHandler messageHandler;
//
//	public static Consumer<DeviceSession> applySessionKeepaliveTimeout(DeviceMessage msg,
//			Supplier<Duration> timeoutSupplier) {
//		return session -> {
//			Duration timeout = msg.getHeader(Headers.keepOnlineTimeoutSeconds).map(Duration::ofSeconds)
//					.orElseGet(timeoutSupplier);
//			if (null != timeout) {
//				session.setKeepAliveTimeout(timeout);
//			}
//		};
//	}
//
//	public Mono<DeviceOperator> handleDeviceMessage(DeviceMessage message,
//			Function<DeviceOperator, DeviceSession> sessionBuilder, Consumer<DeviceSession> sessionConsumer,
//			Runnable deviceNotFoundListener) {
//
//		return handleDeviceMessage(message, sessionBuilder, sessionConsumer,
//				() -> Mono.fromRunnable(deviceNotFoundListener));
//	}
//
//	protected Mono<Void> handleChildrenDeviceMessage(String deviceId, DeviceMessage children) {
//		if (deviceId == null || children instanceof DeviceStateCheckMessage
//				|| children instanceof DeviceStateCheckMessageReply || children instanceof DisconnectDeviceMessage
//				|| children instanceof DisconnectDeviceMessageReply) {
//			return Mono.empty();
//		}
//		if (children instanceof DeviceMessageReply) {
//			DeviceMessageReply reply = ((DeviceMessageReply) children);
//			if (!reply.isSuccess()) {
//				return Mono.empty();
//			}
//		}
//		ChildrenDeviceSession deviceSession = sessionManager.getSession(deviceId, children.getDeviceId());
//		if (deviceSession != null) {
//			deviceSession.keepAlive();
//			applySessionKeepaliveTimeout(children, () -> null).accept(deviceSession);
//		}
//		// 子设备离线或者注销
//		if (children instanceof DeviceOfflineMessage || children instanceof DeviceUnRegisterMessage) {
//			// 注销会话,这里子设备可能会收到多次离线消息
//			// 注销会话一次离线,消息网关转发子设备消息一次
//			if (deviceSession != null && children instanceof DeviceOfflineMessage) {
//				// 忽略离线消息,因为注销会话时,会自动发送一个离线消息
//				children.addHeader(Headers.ignore, true);
//			}
//			return sessionManager.unRegisterChildren(deviceId, children.getDeviceId()).then();
//		}
//		if (deviceSession == null && null != children.getDeviceId()) {
//			// 忽略上线消息,因为注册会话时,会自动发送一个上线消息
//			if (children instanceof DeviceOnlineMessage) {
//				children.addHeader(Headers.ignore, true);
//			}
//			Mono<Void> registerSession = sessionManager.registerChildren(deviceId, children.getDeviceId()).then();
//			// 子设备注册
//			if (isDoRegister(children)) {
//				return Mono.delay(Duration.ofSeconds(2))
//						.then(registry.getDevice(children.getDeviceId()).flatMap(device -> device
//								// 没有配置状态自管理才自动上线
//								.getSelfConfig(DeviceConfigKey.selfManageState).defaultIfEmpty(false)
//								.filter(Boolean.FALSE::equals).flatMap(ignore -> registerSession)));
//			}
//			return registerSession;
//		}
//		return Mono.empty();
//	}
//
//	/**
//	 * 处理来自设备网关的设备消息
//	 *
//	 * @param message                设备消息
//	 * @param sessionBuilder         设备操作
//	 * @param sessionConsumer        设备消费
//	 * @param deviceNotFoundListener 异常监听
//	 * @return 设备操作
//	 */
//	public Mono<DeviceOperator> handleDeviceMessage(DeviceMessage message,
//			Function<DeviceOperator, DeviceSession> sessionBuilder, Consumer<DeviceSession> sessionConsumer,
//			Supplier<Mono<DeviceOperator>> deviceNotFoundListener) {
//		String deviceId = message.getDeviceId();
//		if (StringUtils.isEmpty(deviceId)) {
//			return Mono.empty();
//		}
//		Mono<Void> then = Mono.empty();
//		boolean doHandle = true;
//		if (message instanceof ChildDeviceMessage) {
//			DeviceMessage childrenMessage = (DeviceMessage) ((ChildDeviceMessage) message).getChildDeviceMessage();
//			then = handleChildrenDeviceMessage(deviceId, childrenMessage);
//		} else if (message instanceof ChildDeviceMessageReply) {
//			DeviceMessage childrenMessage = (DeviceMessage) ((ChildDeviceMessageReply) message).getChildDeviceMessage();
//			then = handleChildrenDeviceMessage(deviceId, childrenMessage);
//		} else if (message instanceof DeviceOfflineMessage) {
//			// 设备离线消息
//			DeviceSession session = sessionManager.unregister(deviceId);
//			if (null == session) {
//				// 如果session不存在,则将离线消息转发到
//				return registry.getDevice(deviceId)
//						.flatMap(device -> messageHandler.handleMessage(device, message).thenReturn(device));
//			}
//			return registry.getDevice(deviceId);
//		} else if (message instanceof DeviceOnlineMessage) {
//			// 设备在线消息
//			doHandle = false;
//		}
//		DeviceSession session = sessionManager.getSession(deviceId);
//		// session不存在,可能是同一个连接返回多个设备消息
//		if (session == null) {
//			return registry.getDevice(deviceId).switchIfEmpty(Mono.defer(() -> {
//				// 设备注册
//				if (isDoRegister(message)) {
//					return messageHandler.handleMessage(null, message)
//							// 延迟2秒后尝试重新获取设备并上线
//							.then(Mono.delay(Duration.ofSeconds(2))).then(registry.getDevice(deviceId));
//				}
//				if (deviceNotFoundListener != null) {
//					return deviceNotFoundListener.get();
//				}
//				return Mono.empty();
//			})).flatMap(device -> {
//				// 忽略会话管理,比如一个设备存在多种接入方式时,其中一种接入方式收到的消息设置忽略会话来防止会话冲突
//				if (message.getHeader(Headers.ignoreSession).orElse(false)) {
//					if (!isDoRegister(message)) {
//						return messageHandler.handleMessage(device, message).thenReturn(device);
//					}
//					return Mono.just(device);
//				}
//				// session已经存在了，可能是并发创建.
//				DeviceSession trySession = sessionManager.getSession(deviceId);
//				if (trySession != null) {
//					trySession.keepAlive();
//					return Mono.just(device);
//				}
//				DeviceSession newSession = sessionBuilder.apply(device);
//				if (null != newSession) {
//					// 保持会话，在低功率设备上,可能无法保持长连接.
//					if (message.getHeader(Headers.keepOnline).orElse(false)) {
//						int timeout = message.getHeaderOrDefault(Headers.keepOnlineTimeoutSeconds);
//						newSession = new KeepOnlineSession(newSession, Duration.ofSeconds(timeout));
//					}
//					// 注册会话
//					sessionManager.register(newSession);
//					// 执行自定义会话回调
//					sessionConsumer.accept(newSession);
//					// 保活
//					newSession.keepAlive();
//					if (!(message instanceof DeviceRegisterMessage) && !(message instanceof DeviceOnlineMessage)) {
//						return messageHandler.handleMessage(device, message).thenReturn(device);
//					}
//				}
//				return Mono.just(device);
//			}).switchIfEmpty(then.then(Mono.empty())).flatMap(then::thenReturn);
//		} else {
//			// 消息中指定保存在线
//			if (message.getHeader(Headers.keepOnline).orElse(false) && !(session instanceof KeepOnlineSession)) {
//				Duration timeout = message.getHeader(Headers.keepOnlineTimeoutSeconds).map(Duration::ofSeconds)
//						.orElse(Duration.ofHours(1));
//				// 替换session
//				session = sessionManager.replace(session, new KeepOnlineSession(session, timeout));
//			}
//			sessionConsumer.accept(session);
//			session.keepAlive();
//			if (doHandle) {
//				return messageHandler.handleMessage(session.getOperator(), message).then(then)
//						.then(registry.getDevice(deviceId));
//			}
//			return then.then(registry.getDevice(deviceId));
//		}
//
//	}
//
//	private boolean isDoRegister(DeviceMessage message) {
//		return message instanceof DeviceRegisterMessage && message.getHeader(PropertyConstants.deviceName).isPresent()
//				&& message.getHeader(PropertyConstants.productId).isPresent();
//	}
//
//}
