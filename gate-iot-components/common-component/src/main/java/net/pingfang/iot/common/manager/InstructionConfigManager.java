package net.pingfang.iot.common.manager;

import java.util.List;

import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 11:39
 */
public interface InstructionConfigManager {
	List<InsEntity> getInstruction(Product product);
}
