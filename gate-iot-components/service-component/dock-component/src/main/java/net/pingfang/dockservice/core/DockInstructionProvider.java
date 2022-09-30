package net.pingfang.dockservice.core;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.pingfang.dockservice.DockProduct;
import net.pingfang.dockservice.instruction.DataSummaryInstruction;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.manager.ThroughDataManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 15:40
 */
@Component
public class DockInstructionProvider implements InstructionProvider {
	@Resource
	ThroughDataManager throughDataManager;

	@Override
	public String getName() {
		return DockProduct.DOCK_SERVICE.getValue();
	}

	@Override
	public List<Instruction> getCommand() {
		return Arrays.asList( //
				new DataSummaryInstruction(throughDataManager));
	}
}
