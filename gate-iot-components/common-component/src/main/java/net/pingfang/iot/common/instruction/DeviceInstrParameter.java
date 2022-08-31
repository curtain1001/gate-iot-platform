package net.pingfang.iot.common.instruction;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * <p>
 * 设备指令参数
 * </p>
 *
 * @author 王超
 * @since 2022-08-29 18:32
 */
@Data
public class DeviceInstrParameter {
	String deviceId;
	Map<String, Object> properties;
	JsonNode jsonNode;
}
