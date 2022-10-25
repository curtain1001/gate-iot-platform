package net.pingfang.iot.common.instruction;

import java.util.List;

import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.product.DeviceProduct;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-11 16:26
 */
public interface InstructionManager {

	public void receiveMessage(NetworkMessage networkMessage);

	public Flux<FunctionMessage> subscribe(Long laneId, String deviceId, String instrName);

	public InstructionResult exec(InstructionParam param);

	public Instruction getInstruction(String value);

	public List<Instruction> getInstruction(DeviceProduct deviceProduct);

	public void register(InstructionProvider provider);
}
