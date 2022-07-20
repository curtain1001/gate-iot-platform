package net.pingfang.iot.common.instruction;

import java.util.List;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 18:19
 */
public interface InstructionProvider {
	/**
	 * 默认为设备的产品类型
	 *
	 * @return 代理名称
	 */
	String getName();

	List<Instruction> getCommand();
}
