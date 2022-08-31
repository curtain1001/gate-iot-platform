package net.pingfang.dockservice.customs.instruction;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * <p>
 * 数据汇总服务
 * </p>
 *
 * @author 王超
 * @since 2022-08-16 17:41
 */
@Slf4j
public class DataSummaryInstruction implements BusinessInstruction {
	@Override
	public String getName() {
		return "数据汇总";
	}

	@Override
	public String getValue() {
		return "DATA_SUMMARY";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.down;
	}

	@Override
	public InstructionResult execution(JsonNode jsonNode) {
		log.info("数据汇总,{}", jsonNode);
		JsonNode jsonNode1 = jsonNode.get("DEVICE::008::RECV_REPORT");
		return InstructionResult.success(jsonNode1, "指令执行成功");
	}
}
