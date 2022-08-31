package net.pingfang.flow.values;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionType;
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
@AllArgsConstructor
@NoArgsConstructor
public class NodeProperties {
	/**
	 * 对象类型
	 */
	private ObjectType objectType;
	/**
	 * 指令类型
	 */
	private InstructionType insType;
	/**
	 * 指令
	 */
	private Instruction instruction;

	/**
	 * 设备Id
	 */
	private String deviceId;

	private Map<String, Object> properties;

}
