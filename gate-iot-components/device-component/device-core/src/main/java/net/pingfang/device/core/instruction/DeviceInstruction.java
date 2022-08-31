package net.pingfang.device.core.instruction;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 9:53
 */
public interface DeviceInstruction extends Instruction {

	default ObjectType getObjectType() {
		return ObjectType.device;
	}

	public abstract Product getProduct();

	/**
	 *
	 * @param deviceOperator 设备控制器
	 * @param properties     指令配置
	 * @param jsonNode       指令参数
	 * @return
	 */
	default InstructionResult execution(DeviceOperator deviceOperator, Map<String, Object> properties,
			JsonNode jsonNode) {
		throw new RuntimeException("暂未实现");
	}

}
