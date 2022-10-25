package net.pingfang.iot.common;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 11:04
 */
@Data
@Builder(toBuilder = true)
public class SupportConfigure {
	final Long laneId;
	final String serviceCode;
	final List<Long> deviceIds;
	final Map<String, Object> configure;
}
