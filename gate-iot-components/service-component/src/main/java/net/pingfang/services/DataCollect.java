package net.pingfang.services;

import java.util.concurrent.CompletableFuture;

import net.pingfang.iot.common.GateResult;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-17 10:57
 */
public interface DataCollect {

	public CompletableFuture<GateResult<?>> initiative();

	public CompletableFuture<GateResult<?>> passive();
}
