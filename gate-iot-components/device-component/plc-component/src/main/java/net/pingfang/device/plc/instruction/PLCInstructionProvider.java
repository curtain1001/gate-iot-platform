package net.pingfang.device.plc.instruction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.core.manager.InstructionConfigManager;
import net.pingfang.device.core.utils.ByteUtils;
import net.pingfang.device.plc.PLCDevice;
import net.pingfang.device.plc.PLCProduct;
import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.tcp.TcpMessage;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 11:52
 */
@Component
public class PLCInstructionProvider implements InstructionProvider {
	@Resource
	InstructionConfigManager instructionConfigManager;

	public PLCInstructionProvider(InstructionConfigManager instructionConfigManager) {
		this.instructionConfigManager = instructionConfigManager;

	}

	@Override
	public String getName() {
		return PLCProduct.PLC.name();
	}

	@Override
	public List<Instruction> getCommand() {
		List<InsEntity> entities = instructionConfigManager.getInstruction(PLCProduct.PLC);
		return entities.stream().map(x -> new DeviceInstruction() {
			@Override
			public String getName() {
				return x.getName();
			}

			@Override
			public String getValue() {
				return x.getValue();
			}

			@Override
			public Product getProduct() {
				return PLCProduct.PLC;
			}

			@Override
			public InstructionType getInsType() {
				return InstructionType.valueOf(x.getType());
			}

			@Override
			public Mono<InstructionResult<Object, String>> execution(DeviceOperator deviceOperator) {
				PLCDevice device = (PLCDevice) deviceOperator;
				byte[] bytes = ByteUtils.convertHexStrToByteArray(x.getContent().toString());
				ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
				byteBuf.writeBytes(bytes);
				TcpMessage tcpMessage = new TcpMessage(byteBuf);
				return device.send(tcpMessage).map(y -> {
					if (y != null && y) {
						return InstructionResult.success(true, "指令下发成功");
					}
					return InstructionResult.fail(null, "指令下发失败");
				});
			}

			@Override
			public boolean isSupport(Object object) {
				if (object instanceof TcpMessage) {
					byte[] bytes = ((TcpMessage) object).payloadAsBytes();
					byte[] target = ByteUtils.convertHexStrToByteArray(x.getContent().toString());
					return Arrays.equals(bytes, target);
				}
				return false;
			}
		}).collect(Collectors.toList());
	}
}
