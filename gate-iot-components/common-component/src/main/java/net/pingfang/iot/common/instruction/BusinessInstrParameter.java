package net.pingfang.iot.common.instruction;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

/**
 * <p>
 * 服务指令参数
 * </p>
 *
 * @author 王超
 * @since 2022-08-29 18:32
 */
@Data
public class BusinessInstrParameter {
	JsonNode jsonNode;
}
