package net.pingfang.business.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import net.pingfang.device.core.manager.DefaultDeviceOperatorManager;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 15:55
 */
@Component
public class DefaultInstructionManager implements BeanPostProcessor {

	private final Map<ObjectType, Map<String, Instruction>> objectTypeMap = new ConcurrentHashMap<>();
	private final Map<String, Instruction> insMap = new ConcurrentHashMap<>();
	private final DefaultDeviceOperatorManager operatorManager;
	/**
	 * 指令代理
	 */
	private final Map<String, InstructionProvider> providerSupport = new ConcurrentHashMap<>();

	public DefaultInstructionManager(DefaultDeviceOperatorManager operatorManager) {
		this.operatorManager = operatorManager;
	}

	public void register(Instruction instruction) {
		insMap.put(instruction.getValue(), instruction);
		objectTypeMap.put(instruction.getObjectType(), insMap);
	}

	public void register(InstructionProvider provider) {
		this.providerSupport.put(provider.getName(), provider);
	}

	public void register(List<Instruction> instruction) {
		instruction.forEach(x -> {
			insMap.put(x.getValue(), x);
			objectTypeMap.put(x.getObjectType(), insMap);
		});
	}

	public void refresh() {
		providerSupport.values().forEach(x -> register(x.getCommand()));
	}

	public void deploy(String laneId, String value) {
//		operatorManager.getDevice()

		Instruction instruction = insMap.get(value);

	}

	public Instruction getIns(ObjectType type, String ins) {
		return objectTypeMap.get(type).get(ins);
	}

	public Map<String, Instruction> getIns(ObjectType type) {
		return objectTypeMap.get(type);
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
