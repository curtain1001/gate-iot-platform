package net.pingfang.device.core.instruction;

import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Mono;

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

	default Mono<InstructionResult<Object, String>> execution(DeviceOperator deviceOperator) {
		throw new RuntimeException("暂未实现");
	}

}
