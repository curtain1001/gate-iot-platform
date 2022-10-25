package net.pingfang.iot.common.instruction;

import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description 指令
 * @date 2022-07-04 10:00
 */
public interface Instruction {

	/**
	 * 指令名称
	 *
	 * @return
	 */
	String getName();

	/**
	 * 指令代码
	 *
	 * @return
	 */
	String getValue();

	/**
	 * 指令使用对象
	 *
	 * @return
	 */
	ObjectType getObjectType();

	/**
	 * 指令类型（上行；下行）
	 *
	 * @return
	 */
	InstructionType getInsType();

	DeviceProduct getProduct();

	default NetworkMessage decode(NetworkMessage networkMessage) {
		return networkMessage;
	}

	/**
	 * 指令匹配
	 *
	 * @param object
	 * @return
	 */
	default boolean isSupport(NetworkMessage networkMessage) {
		return false;
	}
}
