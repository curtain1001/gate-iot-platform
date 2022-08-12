package net.pingfang.business.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 15:55
 */
@Component
public class DefaultInstructionManager implements InstructionManager, BeanPostProcessor {
	/**
	 * 指令缓存 key:指令value
	 */
//	private Map<String, Instruction> instrStore = new ConcurrentHashMap<>();

	private final Map<String, ConcurrentMap<String, Instruction>> instrStore = Maps.newConcurrentMap();
	/**
	 * 指令代理
	 */
	private final Map<String, InstructionProvider> providerSupport = new ConcurrentHashMap<>();

	public List<Instruction> getInstruction() {
		return instrStore.values().stream().flatMap(f -> f.values().stream()).collect(Collectors.toList());
	}

	public Instruction getInstruction(String value) {
		return instrStore.values().stream().filter(x -> !x.isEmpty()).map(f -> f.get(value)).filter(Objects::nonNull)
				.findFirst().orElse(null);
	}

	public List<Instruction> getInstruction(Product product) {
		return new ArrayList<>(instrStore.get(product.getValue()).values());
	}

	/**
	 * 注册指令提供类
	 *
	 * @param provider
	 */
	public void register(InstructionProvider provider) {
		this.providerSupport.put(provider.getName(), provider);
		register(provider.getName(), provider.getCommand());
	}

	/**
	 * 注册指令
	 *
	 * @param product
	 * @param instruction
	 */
	public void register(String product, List<Instruction> instruction) {
		this.instrStore.compute(product, (key, value) -> {
			if (value == null) {
				return instruction.stream().collect(Collectors.toConcurrentMap(Instruction::getValue, y -> y));
			} else {
				value.putAll(instruction.stream().collect(Collectors.toMap(Instruction::getValue, y -> y)));
				return value;
			}
		});
	}

	/**
	 * 刷新
	 */
	public void refresh() {
		providerSupport.values().forEach(x -> {
			this.instrStore.compute(x.getName(), (key, value) -> {
				return x.getCommand().stream().collect(Collectors.toConcurrentMap(Instruction::getValue, y -> y));
			});
		});
	}

	/**
	 * 刷新
	 */
	public void refresh(String product) {
		if (StringUtils.isEmpty(product)) {
			refresh();
		} else {
			this.instrStore.compute(product, (key, value) -> {
				return providerSupport.get(product) != null ? providerSupport.get(product).getCommand().stream()
						.collect(Collectors.toConcurrentMap(Instruction::getValue, y -> y)) : Maps.newConcurrentMap();
			});
		}
	}

	public void execute(String laneId, String value) {

	}

	public void deviceExecute(String deviceId, String value) {

	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof InstructionProvider) {
			register((InstructionProvider) bean);
		}
		return bean;
	}

}
