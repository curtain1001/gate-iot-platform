package net.pingfang.business.events;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 14:41
 */
@Data
@Builder(toBuilder = true)
public class ServerNetworkDeleteEvent {
	final String id;
	final String deleteBy;
	final Date deleteTime;
}
