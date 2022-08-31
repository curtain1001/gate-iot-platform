package net.pingfang.business.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import net.pingfang.common.event.EventBusCenter;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.event.MessageUpEvent;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.iot.common.instruction.BusinessInstrParameter;
import net.pingfang.iot.common.instruction.DeviceInstrParameter;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.instruction.InstructionParam;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 15:55
 */
@Component
public class DefaultInstructionManager implements InstructionManager, BeanPostProcessor {

	@Resource
	private DeviceManager deviceManager;
	@Resource
	private EventBusCenter eventBusCenter;
	/**
	 * 指令缓存 key:指令value
	 */
//	private Map<String, Instruction> instrStore = new ConcurrentHashMap<>();

	private final Map<String, ConcurrentMap<String, Instruction>> instrStore = Maps.newConcurrentMap();
	/**
	 * 指令代理
	 */
	private final Map<String, InstructionProvider> providerSupport = new ConcurrentHashMap<>();

	@Override
	public InstructionResult exec(InstructionParam param) {
		InstructionResult result = null;
		Product product = ProductSupports.getSupport(param.getProduct());
		Instruction ins = instrStore.get(product.getValue()).get(param.getInstructionName());
		if (param.getObjectType().equals(ObjectType.service)) {
			BusinessInstrParameter parameter = JsonUtils.toObject(param.getPayload().toString(),
					BusinessInstrParameter.class);
			BusinessInstruction instruction = (BusinessInstruction) ins;
			result = instruction.execution(parameter.getJsonNode());
		} else {

			DeviceInstrParameter parameter = JsonUtils.toObject(param.getPayload().toString(),
					DeviceInstrParameter.class);
			DeviceInstruction instruction = (DeviceInstruction) ins;
			DeviceOperator operator = deviceManager.getDevice(param.getLaneId(), parameter.getDeviceId());
			result = instruction.execution(operator, parameter.getProperties(), parameter.getJsonNode());
		}
		eventBusCenter.postSync(MessageUpEvent.builder() //
				.instruction(ins)//
				.product(product)//
				.message(result)//
				.build());
		return result;
	}

	public List<Instruction> getInstruction() {
		return instrStore.values().stream().flatMap(f -> f.values().stream()).collect(Collectors.toList());
	}

	public Instruction getInstruction(String value) {
		return instrStore.values().stream().filter(x -> !x.isEmpty()).map(f -> f.get(value)).filter(Objects::nonNull)
				.findFirst().orElse(null);
	}

	public List<Instruction> getInstruction(Product product) {
		ConcurrentMap<String, Instruction> store = instrStore.get(product.getValue());
		if (store != null) {
			return new ArrayList<>(store.values());
		}
		return Lists.newArrayList();
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
