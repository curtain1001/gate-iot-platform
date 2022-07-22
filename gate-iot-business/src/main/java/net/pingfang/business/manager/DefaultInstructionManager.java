package net.pingfang.business.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 15:55
 */
@Component
public class DefaultInstructionManager implements BeanPostProcessor {
	/**
	 * 指令缓存 key:指令value
	 */
	private final Map<String, Instruction> instrStore = new ConcurrentHashMap<>();
	/**
	 * 指令代理
	 */
	private final Map<String, InstructionProvider> providerSupport = new ConcurrentHashMap<>();

	private final DefaultDeviceOperatorManager operatorManager;

	public DefaultInstructionManager(DefaultDeviceOperatorManager operatorManager) {
		this.operatorManager = operatorManager;
	}

	public List<Instruction> getInstruction() {
		return new ArrayList<>(instrStore.values());
	}

	public Instruction getInstruction(String value) {
		return instrStore.get(value);
	}

	public List<Instruction> getInstruction(Product product) {
		return instrStore.values().stream().filter(x -> {
			if (ObjectType.device == x.getObjectType()) {
				return ((DeviceInstruction) x).getProduct().isSupported(product.getValue());
			}
			return false;
		}).collect(Collectors.toList());
	}

	public void register(Instruction instruction) {
		instrStore.put(instruction.getValue(), instruction);
	}

	public void register(InstructionProvider provider) {
		this.providerSupport.put(provider.getName(), provider);
	}

	public void register(List<Instruction> instruction) {
		instruction.forEach(x -> {
			instrStore.put(x.getValue(), x);
		});
	}

	public void refresh() {
		providerSupport.values().forEach(x -> register(x.getCommand()));
	}

	public void execute(String laneId, String value) {

	}

	public void deviceExecute(String deviceId, String value) {

	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof InstructionProvider) {
			register((InstructionProvider) bean);
			register((((InstructionProvider) bean).getCommand()));
		}
		return bean;
	}

}
