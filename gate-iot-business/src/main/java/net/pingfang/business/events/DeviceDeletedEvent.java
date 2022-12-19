package net.pingfang.business.events;

import lombok.Builder;
import lombok.Data;

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
public class DeviceDeletedEvent {
	final Long laneId;
	final String deviceNo;

}
