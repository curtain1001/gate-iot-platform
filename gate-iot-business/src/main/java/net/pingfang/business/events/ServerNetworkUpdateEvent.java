package net.pingfang.business.events;

import java.util.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import net.pingfang.network.Control;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 14:40
 */
@Data
@Builder(toBuilder = true)
public class ServerNetworkUpdateEvent {
	final String id;
	final String name;
	final boolean enabled;
	final Map<String, Object> configurations;
	final Control control;
	final String type;
	final String updateBy;
	final Date updateTime;
}
