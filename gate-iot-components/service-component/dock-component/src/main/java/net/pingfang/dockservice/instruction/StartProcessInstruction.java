package net.pingfang.dockservice.instruction;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.dockservice.DockProduct;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 10:31
 */
@Slf4j
public class StartProcessInstruction implements BusinessInstruction {

	@Override
	public String getName() {
		return "开始流程";
	}

	@Override
	public String getValue() {
		return "START_PROCESS";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.up;
	}

	@Override
	public Product getProduct() {
		return DockProduct.DOCK_SERVICE;
	}

	@Override
	public InstructionResult<String, String> execution(JsonNode jsonNode) {
		log.info("开始流程,{}", jsonNode);
		return InstructionResult.success(null, "执行成功");
	}

}
