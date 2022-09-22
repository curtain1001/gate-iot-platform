package net.pingfang.device.novaled.instruction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.scripting.ScriptExecutionContext;
import net.pingfang.common.scripting.nashorn.NashornScriptRunner;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.novaled.NovaLedDeviceProduct;
import net.pingfang.device.novaled.core.NovaLedDevice;
import net.pingfang.device.novaled.utils.ResultCode;
import net.pingfang.iot.common.instruction.FormatType;
import net.pingfang.iot.common.instruction.InsEntity;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.manager.InstructionConfigManager;
import net.pingfang.iot.common.product.Product;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-19 16:04
 */
@Component
@Slf4j
public class NovaLedInstructionProvider implements InstructionProvider {
	@Resource
	InstructionConfigManager instructionConfigManager;

	final static List<FormatType> f = Lists.newArrayList(FormatType.JSON, FormatType.SCRIPT, FormatType.STRING);

	public NovaLedInstructionProvider(InstructionConfigManager instructionConfigManager) {
		this.instructionConfigManager = instructionConfigManager;

	}

	@Override
	public String getName() {
		return NovaLedDeviceProduct.NOVA_LED.name();
	}

	@Override
	public List<Instruction> getCommand() {
		List<InsEntity> entities = instructionConfigManager.getInstruction(NovaLedDeviceProduct.NOVA_LED);

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
				return NovaLedDeviceProduct.NOVA_LED;
			}

			@Override
			public InstructionType getInsType() {
				return InstructionType.valueOf(x.getType());
			}

			@Override
			public InstructionResult<Object, String> execution(DeviceOperator deviceOperator,
					Map<String, Object> properties, JsonNode jsonNode) {
				if (!f.contains(x.getFormat())) {
					log.error(String.format("指令不支持该报文类型:&s", x.getFormat()));
					return InstructionResult.fail(x.getFormat() + "", "指令不支持该报文类型！");
				}
				String content = "";

				if (x.getFormat() == FormatType.SCRIPT) {
					String json = "{\"laneId\":1,\"deviceId\":\"008\",\"product\":\"OCR_License_Plate\",\"instruction\":{\"name\":\"车牌识别上报\",\"value\":\"RECV_REPORT\",\"objectType\":\"device\",\"product\":\"OCR_License_Plate\",\"insType\":\"up\"},\"type\":\"JSON\",\"messageId\":null,\"message\":{\"usWidth\":\"1920\",\"usHeight\":\"1080\",\"ucVehicleColor\":\"4\",\"ucVehicleBrand\":\"0\",\"ucVehicleSize\":\"1\",\"ucPlateColor\":\"1\",\"szLprResult\":\"粤ACM410\",\"usLpBox\":\"[569, 901, 668, 942]\",\"ucLprType\":\"3\",\"usSpeed\":\"0\",\"ucSnapType\":\"2\",\"ucHaveVehicle\":\"\",\"acSnapTime\":\"20220816180928000\\u0000\",\"ucViolateCode\":\"0\",\"ucLaneNo\":\"0\",\"uiVehicleId\":\"42815\",\"ucScore\":\"92\",\"ucDirection\":\"1\",\"ucTotalNum\":\"0\",\"ucSnapshotIndex\":\"0\"}}";
					JsonNode node = JsonUtils.toJsonNode(json);
					NashornScriptRunner runner = new NashornScriptRunner();

					Map<String, Object> sandboxCtx = Maps.newHashMap();
					sandboxCtx.put("_ctx", JsonUtils.nodeToObject(node, Map.class));
					String script = String.format("(function(){try{return %s}catch(err){return '_ERROR_' + err} }())",
							x.getContent());
					String mergedFunc = "(function () { var result = []; " //
							+ "result.push(" + script + ");" //
							+ "return result[0];}())"; //
					ScriptExecutionContext context = ScriptExecutionContext.builder() //
							.fun(mergedFunc)//
							.ctx(sandboxCtx)//
							.build();
					Object v = runner.run(context);
					if (JsonUtils.toJsonString(v).startsWith("_ERROR_")) {
						return InstructionResult.fail(null, v.toString());
					} else {
						content = v.toString();
					}

				}
				NovaLedDevice device = (NovaLedDevice) deviceOperator;
				int i = device.setContent(content);
				if (i == 1) {
					return InstructionResult.success(true, "指令下发成功");
				} else {
					return InstructionResult.fail(i + "", ResultCode.getMsg(i));
				}
			}
		}).collect(Collectors.toList());
	}
}
