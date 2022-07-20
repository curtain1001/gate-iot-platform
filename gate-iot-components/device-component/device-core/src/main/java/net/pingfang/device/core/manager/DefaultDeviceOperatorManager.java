package net.pingfang.device.core.manager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-01 10:24
 */
@Component
@Slf4j
public class DefaultDeviceOperatorManager implements DeviceManager, BeanPostProcessor, ApplicationRunner {

	/**
	 * 设备操作对象缓存
	 */
	private final Map<String, Map<String, DeviceOperator>> store = new ConcurrentHashMap<>();

	/**
	 * 设备操作对象缓存
	 */
	private final Map<String, DeviceOperator> deviceOperatorStore = new ConcurrentHashMap<>();

	/**
	 * 设备代理
	 */
	private final Map<String, DeviceProvider<Object>> providerSupport = new ConcurrentHashMap<>();
	/**
	 * 车道设备对象缓存
	 */
	private final Map<Long, Map<String, DeviceOperator>> laneStore = new ConcurrentHashMap<>();
	/**
	 * 设备配置管理器
	 */
	private final DeviceConfigManager deviceConfigManager;

	public DefaultDeviceOperatorManager(DeviceConfigManager deviceConfigManager,
			ScheduledExecutorService executorService) {
		this.deviceConfigManager = deviceConfigManager;
		executorService.scheduleWithFixedDelay(() -> {
			Flux.fromStream(store.values().stream()).subscribe(d -> d.values().forEach(o -> {
				log.info("执行心跳：{}", JsonUtils.toJsonString(o));
				o.keepAlive();
			}));
		}, 5, 5, TimeUnit.SECONDS);
		executorService.scheduleWithFixedDelay(() -> {
			Flux.fromStream(store.values().stream()).subscribe(d -> d.values().forEach(o -> {
				log.info("设备：{};状态{}", o.getDeviceId(), o.getStatus());

			}));
		}, 10, 13, TimeUnit.SECONDS);
	}

	public List<DeviceOperator> getDevice() {
		List<DeviceOperator> deviceOperators = Lists.newArrayList();
		store.values().forEach(x -> deviceOperators.addAll(x.values()));
		return deviceOperators;
	}

	private Map<String, DeviceOperator> getDeviceStore(String productName) {
		return store.computeIfAbsent(productName, _id -> new ConcurrentHashMap<>());
	}

	private Map<String, DeviceOperator> getLaneDevice(Long laneId) {
		return laneStore.computeIfAbsent(laneId, (x) -> new ConcurrentHashMap<>());
	}

	private Map<String, DeviceOperator> getDeviceStore(Product type) {
		return getDeviceStore(type.getName());
	}

	@Override
	public <T extends DeviceOperator> DeviceOperator create(Product type, String deviceId) {
		return getDevice(type, deviceId);
	}

	public <T extends DeviceOperator> DeviceOperator getDevice(Product type, String id, Boolean create) {
		Map<String, DeviceOperator> operatorMap = getDeviceStore(type);
		DeviceOperator deviceOperator = operatorMap.get(id);
		if (create && (deviceOperator == null || !deviceOperator.isAlive())) {
			deviceOperator = createDeviceOperator(type, id);
		}
		return deviceOperator;
	}

	@Override
	public <T extends DeviceOperator> DeviceOperator getDevice(Product type, String id) {
		Map<String, DeviceOperator> operatorMap = getDeviceStore(type);
		DeviceOperator deviceOperator = operatorMap.get(id);
		if (deviceOperator == null || !deviceOperator.isAlive()) {
			deviceOperator = createDeviceOperator(type, id);
		}
		return deviceOperator;
	}

	public DeviceOperator createDeviceOperator(Product type, String id) {
		DeviceProvider<Object> provider = providerSupport.get(type.getName());
		if (provider == null) {
			throw new UnsupportedOperationException("不支持的类型:" + type.getName());
		} else {
			DeviceProperties properties = deviceConfigManager.getProperties(type, id);
			if (properties == null) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]不存在");
			} else if (!properties.isEnabled()) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]已禁用");
			}
			Object config = provider.createConfig(properties);
			return doCreate(provider, id, config);
		}
	}

	/**
	 * 如果store中不存在网络组件就创建，存在就重新加载
	 *
	 * @param provider   网络组件支持提供商
	 * @param id         网络组件唯一标识
	 * @param properties 网络组件配置
	 * @return 网络组件
	 */
	public DeviceOperator doCreate(DeviceProvider<Object> provider, String id, Object properties) {
		return getDeviceStore(provider.getType()).compute(id, (s, operator) -> {
			if (operator == null) {
				operator = provider.createDevice(properties);
				operator.subscribe()
						.subscribe(x -> log.info("command" + JsonUtils.toJsonNode(x).get("command").asText()));
				log.info("设备启动成功：deviceId:{},deviceName:{}", operator.getDeviceId(), operator.getDeviceName());
			} else {
				// 单例，已经存在则重新加载
				provider.reload(operator, properties);
			}
			return operator;
		});
	}

	@Override
	public List<DeviceProvider<?>> getProviders() {
		return new ArrayList<>(providerSupport.values());
	}

	@Override
	public void reload(Product type, String id) {
		getDeviceStore(type).compute(id, (s, operator) -> {
			if (operator != null) {
				operator.disconnect();
			}
			return null;
		});
		getDevice(type, id);
	}

	@Override
	public void shutdown(Product type, String deviceId) {
		getDeviceStore(type).computeIfPresent(deviceId, (s, operator) -> {
			operator.disconnect();
			getLaneDevice(operator.getLaneId()).computeIfPresent(deviceId, (l, ld) -> null);
			getLaneDevice(operator.getLaneId()).remove(deviceId);
			return null;
		});
		getDeviceStore(type).remove(deviceId);
	}

	public void register(DeviceProvider<Object> provider) {
		this.providerSupport.put(provider.getType().getName(), provider);
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof DeviceProvider) {
			register(((DeviceProvider) bean));
		}
		return bean;
	}

	/**
	 * 将所有需要启动的设备启动起来，同时进行设备状态检测
	 */
	@PostConstruct
	public void check() {
		Flux.interval(Duration.ofSeconds(10)).subscribe(x -> {
			try {
				this.checkDevice();
			} catch (Exception e) {
				log.error("任务启动失败:", e);
			}
		});
	}

	/**
	 * 检查网络 把需要加载的网络组件启动起来
	 */
	protected void checkDevice() {
		// 获取并过滤所有停止的网络组件
		// 重新加载启动状态的网络组件
		Collection<Map<String, DeviceOperator>> operators = store.values();
		operators.stream().flatMap(m -> m.values().stream()).filter(x -> !x.isAlive()).forEach(operator -> {
			DeviceProvider<Object> provider = providerSupport.get(operator.getProduct().getName());
			if (provider == null || !operator.isAutoReload()) {
				return;
			}
			DeviceProperties properties = deviceConfigManager.getProperties(operator.getProduct(),
					operator.getDeviceId());
			if (properties != null && properties.isEnabled()) {
				Object o = provider.createConfig(properties);
				this.doCreate(provider, operator.getDeviceId(), o);
			}
		});
	}

	@PreDestroy
	public void destroy() {
		this.store.values().forEach(x -> x.values().forEach(DeviceOperator::disconnect));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		startDevice();
	}

	protected void startDevice() {
		List<DeviceProperties> properties = deviceConfigManager.getProperties();
		properties.stream().filter(DeviceProperties::isEnabled).forEach(pro -> {
			DeviceProvider<Object> provider = providerSupport.get(pro.getProduct().getName());
			if (pro.isEnabled()) {
				Object o = provider.createConfig(pro);
				this.doCreate(provider, pro.getDeviceId(), o);
			}
		});
	}
}
