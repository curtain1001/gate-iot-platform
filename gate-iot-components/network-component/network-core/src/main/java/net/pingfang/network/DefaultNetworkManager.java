package net.pingfang.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.manager.SessionManager;
import net.pingfang.iot.common.network.NetworkType;
import reactor.core.publisher.Mono;

/**
 * 默认网络管理器
 *
 * @author zhouhao
 */
@Component
@Slf4j
public class DefaultNetworkManager implements NetworkManager, BeanPostProcessor {

	private final NetworkConfigManager configManager;

	private final SessionManager sessionManager;

	private final Map<String, Map<String, Network>> store = new ConcurrentHashMap<>();

	private final Map<String, NetworkProvider<Object>> providerSupport = new ConcurrentHashMap<>();

	public DefaultNetworkManager(NetworkConfigManager configManager, SessionManager sessionManager) {
		this.configManager = configManager;
		this.sessionManager = sessionManager;
	}

	@Override
	public void reload(NetworkType type, String id) {
		Mono.justOrEmpty(getNetworkStore(type) //
				.get(id)) //
				.doOnNext(Network::shutdown) //
				.then(Mono.just(getNetwork(type, id))) //
				.subscribe();
	}

//	@PostConstruct
//	public void start() {
//		Flux.interval(Duration.ofSeconds(10)).subscribe(t -> this.checkNetwork());
//	}

//	/**
//	 * 检查网络 把需要加载的网络组件启动起来
//	 */
//	protected void checkNetwork() {
//		try {
//			// 获取并过滤所有停止的网络组件
//			// 重新加载启动状态的网络组件
//			Collection<Map<String, Network>> networks = store.values();
//			networks.stream().flatMap(m -> m.values().stream()).filter(x -> !x.isAlive()).forEach(network -> {
//				NetworkProvider<Object> provider = providerSupport.get(network.getType().getId());
//				// 记录状态
//				configManager.update(network.getId(), "disabled");
//				if (provider == null || !network.isAutoReload()) {
//					return;
//				}
//				NetworkProperties properties = configManager.getConfig(network.getId());
//				if (properties != null && properties.isEnabled()) {
//					// 记录状态
//					configManager.update(network.getId(), "reboot");
//					try {
//						Object o = provider.createConfig(properties);
//						this.doCreate(provider, network.getId(), o);
//					} catch (Exception e) {
//						log.error("网络组件启动失败：", e);
//					}
//				}
//			});
//		} catch (Exception e) {
//			log.error("网络组件启动失败：", e);
//		}
//
//	}

	private Map<String, Network> getNetworkStore(String type) {
		return store.computeIfAbsent(type, _id -> new ConcurrentHashMap<>());
	}

	private Map<String, Network> getNetworkStore(NetworkType type) {
		return getNetworkStore(type.getId());
	}

	@Override
	public Map<String, Network> getNetwork(NetworkType type) {
		return getNetworkStore(type);
	}

	@Override
	public <T extends Network> Network getNetwork(NetworkType type, String id) {
		Map<String, Network> networkMap = getNetworkStore(type);
		Network network = networkMap.get(id);
		if (network == null || !network.isAlive()) {
			network = createNetwork(type, id);
		}
		return network;
	}


	/**
	 * 如果store中不存在网络组件就创建，存在就重新加载
	 *
	 * @param provider   网络组件支持提供商
	 * @param id         网络组件唯一标识
	 * @param properties 网络组件配置
	 * @return 网络组件
	 */
	public Network doCreate(NetworkProvider<Object> provider, String id, Object properties) {
		return getNetworkStore(provider.getType()).compute(id, (s, network) -> {
			if (network == null) {
				network = provider.createNetwork(properties);
				sessionManager.register(network.getId(), network);
			} else {
				// 单例，已经存在则重新加载
				provider.reload(network, properties);
//				Network finalNetwork = network;
//				AsyncManager.me().execute(new TimerTask() {
//					@Override
//					public void run() {
//						if (finalNetwork.isAlive()) {
//							configManager.update(finalNetwork.getId(), "enabled");
//						}
//					}
//				});
			}
			return network;
		});
	}


	public Network createNetwork(NetworkType type, String id) {
		NetworkProvider<Object> provider = providerSupport.get(type.getId());
		if (provider == null) {
			throw new UnsupportedOperationException("不支持的类型:" + type.getName());
		} else {
			NetworkProperties properties = configManager.getConfig(id);
			if (properties == null) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]不存在");
			} else if (!properties.isEnabled()) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]已禁用");
			}
			Object config = null;
			try {
				config = provider.createConfig(properties);
			} catch (Exception e) {
				log.error("设备配置创建失败;", e);
				throw new RuntimeException("设备配置创建失败");
			}
			return doCreate(provider, id, config);
		}
	}

	public void register(NetworkProvider<Object> provider) {
		this.providerSupport.put(provider.getType().getId(), provider);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof NetworkProvider) {
			register(((NetworkProvider) bean));
		}
		return bean;
	}

	@Override
	public List<NetworkProvider<?>> getProviders() {
		return new ArrayList<>(providerSupport.values());
	}

	@Override
	public void shutdown(NetworkType type, String id) {
		Network network = getNetworkStore(type).get(id);
		if (network != null) {
			getNetworkStore(type).remove(id);
			sessionManager.shutdown(id);
			network.shutdown();
		}
	}

}
