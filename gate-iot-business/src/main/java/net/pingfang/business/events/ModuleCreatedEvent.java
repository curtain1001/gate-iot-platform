package net.pingfang.business.events;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import net.pingfang.gate.protocol.values.DeviceInfo;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-23 17:44
 */
@Data
@Builder
public class ModuleCreatedEvent {
	final Long laneId;
	final String moduleCode;
	final String moduleName;
	final List<DeviceInfo> deviceInfos;
	final Map<String, Object> configuration;
}
