package net.pingfang.business.manager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.domain.BtpServer;
import net.pingfang.business.service.IBtpServerService;
import net.pingfang.iot.common.product.Product;
import net.pingfang.servicecomponent.core.ServerManager;
import net.pingfang.servicecomponent.core.ServerOperator;
import net.pingfang.servicecomponent.core.ServerProperties;
import net.pingfang.servicecomponent.core.ServerProvider;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * <p>
 * 默认服务管理器
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 16:11
 */
@Component
@Slf4j
public class DefaultServerManager implements ServerManager, BeanPostProcessor, ApplicationRunner {

	@Resource
	public IBtpServerService iBtpServerService;

	private final EmitterProcessor<ServerOperator> processor = EmitterProcessor.create(true);
	private final FluxSink<ServerOperator> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	/**
	 * 设备代理
	 */
	private final Map<String, ServerProvider<Object>> providerSupport = new ConcurrentHashMap<>();
	/**
	 * 车道设备对象缓存
	 */
	private final Map<String, Map<String, ServerOperator>> store = new ConcurrentHashMap<>();

	@Override
	public <T extends ServerOperator> ServerOperator getServer(Long laneId, String serverId) {
		Map<String, ServerOperator> operatorMap = getServerStore(laneId);
		return operatorMap.get(serverId);
	}

	@Override
	public <T extends ServerOperator> List<ServerOperator> getServers(Product product) {
		return store.values().stream().flatMap(x -> x.values().stream()).filter(x -> x.getProduct() == product)
				.collect(Collectors.toList());
	}

	@Override
	public <T extends ServerOperator> ServerOperator create(Long laneId, String serverId, Product type) {
		Map<String, ServerOperator> operatorMap = getServerStore(laneId);
		ServerOperator serverOperator = operatorMap.get(serverId);
		if (serverOperator == null || !serverOperator.isAlive()) {
			serverOperator = createServerOperator(type, serverId);
			received(serverOperator);
		}
		return serverOperator;
	}

	public List<ServerOperator> getServer() {
		return store.values().stream().flatMap(f -> f.values().stream()).collect(Collectors.toList());
	}

	private Map<String, ServerOperator> getServerStore(Long laneId) {
		return store.computeIfAbsent(getLaneId(laneId), _id -> new ConcurrentHashMap<>());
	}

	public ServerOperator createServerOperator(Product type, String id) {
		ServerProvider<Object> provider = providerSupport.get(type.getName());
		if (provider == null) {
			throw new UnsupportedOperationException("不支持的类型:" + type.getName());
		} else {
			ServerProperties properties = iBtpServerService.getProperties(id);
			if (properties == null) {
				throw new UnsupportedOperationException("服务[" + type.getName() + "]配置[" + id + "]不存在");
			} else if (!properties.isEnabled()) {
				throw new UnsupportedOperationException("服务[" + type.getName() + "]配置[" + id + "]已禁用");
			}
			Object config = provider.createConfig(properties);
			return doCreate(provider, properties.getLaneId(), id, config);
		}
	}

	/**
	 * 如果store中不存在设备就创建，存在就重新加载
	 *
	 * @param provider   设备类型支持提供商
	 * @param laneId     车道
	 * @param deviceId   设备唯一标识
	 * @param properties 设备配置
	 * @return 网络组件
	 */
	public ServerOperator doCreate(ServerProvider<Object> provider, Long laneId, String deviceId, Object properties) {
		return getServerStore(laneId).compute(deviceId, (s, operator) -> {
			if (operator == null) {
				operator = provider.createServer(properties);
				log.info("服务启动成功：serverId:{},serverName:{}", operator.getServerId(), operator.getServerName());
			} else {
				// 单例，已经存在则重新加载
				provider.reload(operator, properties);
				log.info("服务重启成功：serverId:{},serverName:{}", operator.getServerId(), operator.getServerName());
			}
			return operator;
		});
	}

	@Override
	public List<ServerProvider<?>> getProviders() {
		return new ArrayList<>(providerSupport.values());
	}

	@Override
	public void reload(Long laneId, String serverId) {
		getServerStore(laneId).computeIfPresent(serverId, (s, operator) -> {
			ServerProvider<Object> provider = providerSupport.get(operator.getProduct().getName());
			ServerProperties properties = iBtpServerService.getProperties(serverId);
			Object config = provider.createConfig(properties);
			provider.reload(operator, config);
			log.info("服务重新启动成功：serverId:{},serverName:{}", operator.getServerId(), operator.getServerName());
			return operator;
		});
	}

	@Override
	public void shutdown(Long laneId, String serverId) {
		getServerStore(laneId).computeIfPresent(serverId, (s, operator) -> {
			operator.close();
			return null;
		});
		getServerStore(laneId).remove(serverId);
	}

	@Override
	public Flux<ServerOperator> subscribe() {
		return processor.map(Function.identity());
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			startServer();
		} catch (Exception e) {
			log.error("服务启动失败：", e);
		}
	}

	public void register(ServerProvider<Object> provider) {
		this.providerSupport.put(provider.getType().getName(), provider);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ServerProvider) {
			register(((ServerProvider) bean));
		}
		return bean;
	}

	protected void startServer() {
		LambdaQueryWrapper<BtpServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpServer::getEnabled, 0);
		List<BtpServer> btpServers = iBtpServerService.list(queryWrapper);
		btpServers.stream().map(BtpServer::toProperties).forEach(pro -> {
			if (pro.isEnabled()) {
				create(pro.getLaneId(), pro.getServerId(), pro.getProduct());
			}
		});
	}

	/**
	 * 将所有需要启动的设备启动起来，同时进行设备状态检测
	 */
	@PostConstruct
	public void check() {
		Flux.interval(Duration.ofSeconds(10)).subscribe(x -> {
			try {
				this.checkServer();
			} catch (Exception e) {
				log.error("任务启动失败:", e);
			}
		});
	}

	/**
	 * 检查网络 把需要加载的设备启动起来
	 */
	protected void checkServer() {
		// 获取并过滤所有停止的网络组件
		// 重新加载启动状态的网络组件
		Collection<Map<String, ServerOperator>> operators = store.values();
		operators.stream().flatMap(m -> m.values().stream()).filter(x -> !x.isAlive()).forEach(operator -> {
			ServerProvider<Object> provider = providerSupport.get(operator.getProduct().getName());
			if (provider == null || !operator.isAutoReload()) {
				return;
			}
			ServerProperties properties = iBtpServerService.getProperties(operator.getServerId());
			if (properties != null && properties.isEnabled()) {
				Object o = provider.createConfig(properties);
				this.doCreate(provider, properties.getLaneId(), operator.getServerId(), o);
			}
		});
	}

	@PreDestroy
	public void destroy() {
		this.store.values().forEach(x -> x.values().forEach(ServerOperator::close));
	}

	public void received(ServerOperator serverOperator) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn(" message pending {} ,drop message:{}", processor.getPending(), serverOperator.toString());
			return;
		}
		sink.next(serverOperator);
	}

	/**
	 * 存在车道为空的公共服务时，采用null key储存
	 *
	 * @param laneId
	 * @return
	 */
	protected String getLaneId(Long laneId) {
		return String.valueOf(laneId);
	}
}
