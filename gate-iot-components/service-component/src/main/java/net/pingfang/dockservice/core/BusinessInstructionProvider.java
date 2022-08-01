package net.pingfang.dockservice.core;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import net.pingfang.dockservice.DockProduct;
import net.pingfang.dockservice.customs.instruction.CustomsInstruction;
import net.pingfang.dockservice.customs.instruction.StartProcessInstruction;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 15:40
 */
@Component
public class BusinessInstructionProvider implements InstructionProvider {
	@Override
	public String getName() {
		return DockProduct.DOCK_SERVICE.getName();
	}

	@Override
	public List<Instruction> getCommand() {
		return Arrays.asList( //
				new CustomsInstruction(), //
				new StartProcessInstruction() //
		);
	}
}
