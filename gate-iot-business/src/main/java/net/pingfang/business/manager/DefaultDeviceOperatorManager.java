package net.pingfang.business.manager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

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
import net.pingfang.device.core.manager.DeviceConfigManager;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-01 10:24
 */
@Component
@Slf4j
public class DefaultDeviceOperatorManager implements DeviceManager, BeanPostProcessor, ApplicationRunner {

//	@Resource
//	EventBusCenter eventBusCenter;
//
//	@Resource
//	DefaultInstructionManager instructionManager;

	private final EmitterProcessor<DeviceOperator> processor = EmitterProcessor.create(true);
	private final FluxSink<DeviceOperator> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	/**
	 * 设备代理
	 */
	private final Map<String, DeviceProvider<Object>> providerSupport = new ConcurrentHashMap<>();
	/**
	 * 车道设备对象缓存
	 */
	private final Map<Long/* 车道id */, Map<String/* 设备号 */, DeviceOperator>> store = new ConcurrentHashMap<>();

	/**
	 * 设备配置管理器
	 */
	private final DeviceConfigManager deviceConfigManager;

	public DefaultDeviceOperatorManager(DeviceConfigManager deviceConfigManager,
			ScheduledExecutorService executorService) {
		this.deviceConfigManager = deviceConfigManager;

//		executorService.scheduleWithFixedDelay(() -> {
//			Flux.fromStream(store.values().stream()).subscribe(d -> d.values().forEach(o -> {
//				log.info("执行心跳：{}", JsonUtils.toJsonString(o));
////				o.keepAlive();
//			}));
//		}, 5, 5, TimeUnit.SECONDS);
		executorService.scheduleWithFixedDelay(() -> {
			Flux.fromStream(store.values().stream()).subscribe(d -> d.values().forEach(o -> {
				log.info("设备：{};状态{}", o.getDeviceId(), o.getStatus());

			}));
		}, 25, 25, TimeUnit.SECONDS);
	}

	public List<DeviceOperator> getDevice() {
		return store.values().stream().flatMap(f -> f.values().stream()).collect(Collectors.toList());
	}

	private Map<String, DeviceOperator> getDeviceStore(Long laneId) {
		return store.computeIfAbsent(laneId, _id -> new ConcurrentHashMap<>());
	}

	@Override
	public <T extends DeviceOperator> DeviceOperator create(Long laneId, String deviceId, Product type) {
		Map<String, DeviceOperator> operatorMap = getDeviceStore(laneId);
		DeviceOperator deviceOperator = operatorMap.get(deviceId);
		if (deviceOperator == null || !deviceOperator.isAlive()) {
			deviceOperator = createDeviceOperator(type, deviceId);
			this.received(deviceOperator);
		}
		return deviceOperator;

	}

	@Override
	public <T extends DeviceOperator> DeviceOperator getDevice(Long laneId, String deviceId) {
		Map<String, DeviceOperator> operatorMap = getDeviceStore(laneId);
		return operatorMap.get(deviceId);
	}

	@Override
	public <T extends DeviceOperator> List<DeviceOperator> getDevices(Product product) {
		return store.values().stream().flatMap(x -> x.values().stream()).filter(x -> x.getProduct() == product)
				.collect(Collectors.toList());
	}

	public DeviceOperator createDeviceOperator(Product type, String id) {
		DeviceProvider<Object> provider = providerSupport.get(type.getName());
		if (provider == null) {
			throw new UnsupportedOperationException("不支持的类型:" + type.getName());
		} else {
			DeviceProperties properties = deviceConfigManager.getProperties(id);
			if (properties == null) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]不存在");
			} else if (!properties.isEnabled()) {
				throw new UnsupportedOperationException("网络[" + type.getName() + "]配置[" + id + "]已禁用");
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
	public DeviceOperator doCreate(DeviceProvider<Object> provider, Long laneId, String deviceId, Object properties) {
		return getDeviceStore(laneId).compute(deviceId, (s, operator) -> {
			if (operator == null) {
				operator = provider.createDevice(properties);
				log.info("设备启动成功：deviceId:{},deviceName:{}", operator.getDeviceId(), operator.getDeviceName());
//				// 发布事件
//				operator.subscribe(1L).subscribe(x -> log.warn("订阅1车道的设备"));
//				operator.subscribe(null).doOnError(x -> log.error("订阅设备消息异常：" + x.getMessage())).subscribe(x -> {
//					try {
//						List<Instruction> instructions = instructionManager.getInstruction(x.getProduct());
//						if (CollectionUtils.isNotEmpty(instructions)) {
//							Optional<Instruction> optional = instructions.stream()
//									.filter(i -> i.getInsType() == InstructionType.up && i.isSupport(x.getPayload()))
//									.findFirst();
//							optional.ifPresent(instruction -> eventBusCenter.postAsync(MessageUpEvent.builder() //
//									.laneId(x.getLaneId()) //
//									.product(x.getProduct())//
//									.deviceId(x.getDeviceId()) //
//									.instruction(instruction)//
//									.message(x.getPayload()) //
//									.type(x.getType())//
//									.build()));
//						} else {
//							eventBusCenter.postAsync(MessageUpEvent.builder() //
//									.laneId(x.getLaneId()) //
//									.product(x.getProduct())//
//									.deviceId(x.getDeviceId()) //
//									.message(x.getPayload()) //
//									.type(x.getType())//
//									.build());
//						}
//					} catch (Exception e) {
//						log.error("消费车道设备消息异常：", e);
//					}
//				});
			} else {
				// 单例，已经存在则重新加载
				provider.reload(operator, properties);
				log.info("设备重启成功：deviceId:{},deviceName:{}", operator.getDeviceId(), operator.getDeviceName());
			}

			return operator;
		});
	}

	@Override
	public List<DeviceProvider<?>> getProviders() {
		return new ArrayList<>(providerSupport.values());
	}

	@Override
	public void reload(Long laneId, String deviceId) {
		getDeviceStore(laneId).computeIfPresent(deviceId, (s, operator) -> {
			DeviceProvider<Object> provider = providerSupport.get(operator.getProduct().getName());
			DeviceProperties properties = deviceConfigManager.getProperties(deviceId);
			Object config = provider.createConfig(properties);
			provider.reload(operator, config);
			log.info("设备重新启动成功：deviceId:{},deviceName:{}", operator.getDeviceId(), operator.getDeviceName());
			return operator;
		});
	}

	@Override
	public void shutdown(Long laneId, String deviceId) {
		getDeviceStore(laneId).computeIfPresent(deviceId, (s, operator) -> {
			operator.shutdown();
			return null;
		});
		getDeviceStore(laneId).remove(deviceId);
	}

	@Override
	public Flux<DeviceOperator> subscribe() {
		return processor.map(Function.identity());
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
	 * 检查网络 把需要加载的设备启动起来
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
			DeviceProperties properties = deviceConfigManager.getProperties(operator.getDeviceId());
			if (properties != null && properties.isEnabled()) {
				Object o = provider.createConfig(properties);
				this.doCreate(provider, properties.getLaneId(), operator.getDeviceId(), o);
			}
		});
	}

	@PreDestroy
	public void destroy() {
		this.store.values().forEach(x -> x.values().forEach(DeviceOperator::shutdown));
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		startDevice();
	}

	protected void startDevice() {

		List<DeviceProperties> properties = deviceConfigManager.getProperties();
		properties.stream().filter(DeviceProperties::isEnabled).forEach(pro -> {
			this.create(pro.getLaneId(), pro.getDeviceId(), pro.getProduct());
		});
	}

	public void received(DeviceOperator operator) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn(" message pending {} ,drop message:{}", processor.getPending(), JsonUtils.toJsonString(operator));
			return;
		}
		sink.next(operator);
	}
}
