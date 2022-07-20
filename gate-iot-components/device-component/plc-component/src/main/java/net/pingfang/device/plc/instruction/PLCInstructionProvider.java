package net.pingfang.device.plc.instruction;

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
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.tcp.TcpMessage;

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
			public void execution(DeviceOperator deviceOperator) {
				PLCDevice device = (PLCDevice) deviceOperator;
				byte[] bytes = ByteUtils.convertHexStrToByteArray(x.getContent().toString());
				ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
				byteBuf.writeBytes(bytes);
				TcpMessage tcpMessage = new TcpMessage(byteBuf);
				device.send(tcpMessage);
			}

			@Override
			public boolean isSupport(Object object) {
				byte[] bytes = ByteUtils.convertHexStrToByteArray(x.getContent().toString());
				ByteBuf target = ByteBufAllocator.DEFAULT.ioBuffer();
				target.writeBytes(bytes);
				ByteBuf byteBuf = (ByteBuf) object;
				return target.equals(byteBuf);
			}
		}).collect(Collectors.toList());
	}
}
