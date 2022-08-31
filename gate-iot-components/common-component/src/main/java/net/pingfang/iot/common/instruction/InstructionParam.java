package net.pingfang.iot.common.instruction;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-29 18:29
 */
@Data
public class InstructionParam {
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 指令对象
	 */
	private ObjectType objectType;

	private String product;

	private String instructionName;

	private Object payload;

}
