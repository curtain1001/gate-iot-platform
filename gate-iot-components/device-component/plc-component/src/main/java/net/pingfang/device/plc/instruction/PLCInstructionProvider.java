package net.pingfang.device.plc.instruction;

import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.core.utils.ByteUtils;
import net.pingfang.device.plc.PLCDevice;
import net.pingfang.device.plc.PLCProduct;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.manager.InstructionConfigManager;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.tcp.TcpMessage;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-06 11:52
 */
@Component
@Slf4j
public class PLCInstructionProvider implements InstructionProvider {
	@Resource
	InstructionConfigManager instructionConfigManager;

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
			public InstructionResult<Object, String> execution(DeviceOperator deviceOperator,
					Map<String, Object> properties, JsonNode jsonNode) {
				PLCDevice device = (PLCDevice) deviceOperator;
				byte[] bytes = ByteUtils.convertHexStrToByteArray(x.getContent().toString());
				ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
				byteBuf.writeBytes(bytes);
				TcpMessage tcpMessage = new TcpMessage(byteBuf);
				Boolean bln = device.send(tcpMessage).block(Duration.ofMinutes(1L));
				if (bln != null && bln) {
					return InstructionResult.success(true, "指令下发成功");
				} else {
					return InstructionResult.fail(null, "指令下发失败");
				}
			}

			@Override
			public boolean isSupport(NetworkMessage networkMessage) {
				return networkMessage.payloadAsString().toUpperCase(Locale.ROOT)
						.equals(x.getContent().toString().toUpperCase(Locale.ROOT));
//				byte[] target = ByteUtils.convertHexStrToByteArray();
//				return Arrays.equals((byte[]) object, target);
			}
		}).collect(Collectors.toList());
	}
}
