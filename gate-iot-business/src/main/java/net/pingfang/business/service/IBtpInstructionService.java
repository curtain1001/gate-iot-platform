package net.pingfang.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpInstruction;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 23:42
 */
public interface IBtpInstructionService extends IService<BtpInstruction> {
	public List<Instruction> getInstructions(Product product);
}
