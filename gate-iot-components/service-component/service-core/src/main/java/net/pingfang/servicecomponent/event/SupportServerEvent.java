package net.pingfang.servicecomponent.event;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 14:37
 */
@Data
@Builder(toBuilder = true)
public class SupportServerEvent {
	final Long id;
	final String product;
	final Map<String, Object> configuration;
}
