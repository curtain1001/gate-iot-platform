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
public class DockGateInstruction implements BusinessInstruction {

	@Override
	public String getName() {
		return "对接云闸口平台";
	}

	@Override
	public String getValue() {
		return "DOCK_GATE_PLATFORM";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.down;
	}

	@Override
	public Product getProduct() {
		return DockProduct.DOCK_SERVICE;
	}

	@Override
	public InstructionResult<String, String> execution(JsonNode jsonNode) {
		log.info("对接海关{}", jsonNode);
//		HttpUtils.sendGet()
		return InstructionResult.success(null, "执行成功");
	}

}
