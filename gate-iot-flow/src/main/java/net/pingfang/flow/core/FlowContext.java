package net.pingfang.flow.core;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 18:16
 */
@Data
@Builder
public class FlowContext {
	Long laneId;

	Map<String, Object> content;
}
