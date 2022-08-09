package net.pingfang.flow.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-08 15:18
 */
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeProperties {
	/**
	 * 对象类型
	 */
	private ObjectType objectType;
	/**
	 * 指令类型
	 */
	private String insType;
	/**
	 * 指令
	 */
	private Instruction instruction;

	/**
	 * 设备Id
	 */
	private String deviceId;
}
