package net.pingfang.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import net.pingfang.iot.common.instruction.InstructionManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-14 17:39
 */
@Component
public class LaneControlService {
	private final InstructionManager instructionManager;

	public LaneControlService(InstructionManager instructionManager) {
		this.instructionManager = instructionManager;
	}

	public CompletableFuture<?> come() {
		return CompletableFuture.runAsync(() -> instructionManager.subscribe(null, null, null).take(1));
	}
}
