package net.pingfang.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.licenseplate.instruction.ImageSnap;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.GateResult;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.manager.SupportServiceConfigManager;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 箱号识别服务
 * </p>
 *
 * @author 王超
 * @since 2022-10-17 10:39
 */
@Slf4j
public class ContainerOcrService extends IGateService implements DataCollect {
	private final Long laneId;
	private final String deviceId;
	private final InstructionManager instructionManager;
	private final DeviceManager deviceManager;
	private final SupportServiceConfigManager configManager;

	public ContainerOcrService(Long laneId, String deviceId, RabbitTemplate rabbitTemplate, String fanoutExchange,
			InstructionManager instructionManager, DeviceManager deviceManager,
			SupportServiceConfigManager configManager) {
		super(rabbitTemplate, fanoutExchange);
		this.laneId = laneId;
		this.deviceId = deviceId;
		this.instructionManager = instructionManager;
		this.deviceManager = deviceManager;
		this.configManager = configManager;
	}

	@Override
	public CompletableFuture<GateResult<?>> initiative() {
		Mono<FunctionMessage> mono = instructionManager.subscribe(laneId, null, "").next();
		return mono.toFuture().handleAsync((x, throwable) -> {
			if (throwable != null) {
				log.error("车牌识别获取失败：{}", throwable.getLocalizedMessage());
				return GateResult.fail("获取信息异常:", throwable.getMessage());
			}
			return GateResult.ok(x.getPayload());
		});
	}

	@Override
	public CompletableFuture<GateResult<?>> passive() {
		DeviceOperator deviceOperator = deviceManager.getDevice(laneId, deviceId);
		ImageSnap snap = new ImageSnap();

		Mono<FunctionMessage> mono = instructionManager.subscribe(laneId, null, "").next();
		CompletableFuture<GateResult<?>> gateResultCompletableFuture = mono.toFuture().handleAsync((x, throwable) -> {
			if (throwable != null) {
				log.error("车牌识别获取失败：{}", throwable.getLocalizedMessage());
				return GateResult.fail("获取信息异常:", throwable.getMessage());
			}
			return GateResult.ok(x.getPayload());
		});
		InstructionResult<String, String> result = snap.execution(deviceOperator, null, null);
		if (result.isSuccess()) {
			return gateResultCompletableFuture;
		} else {
			return CompletableFuture.completedFuture(GateResult.fail("获取信息异常:" + result.getMessage()));
		}
	}

	@Override
	public void post(Long laneId) {

	}

	@Override
	public void post() {

	}
}
