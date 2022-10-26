package net.pingfang.services.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 22:50
 */
@AllArgsConstructor
@Data
@Builder
public class StartGateFlowEvent {
	final long laneId;
}
