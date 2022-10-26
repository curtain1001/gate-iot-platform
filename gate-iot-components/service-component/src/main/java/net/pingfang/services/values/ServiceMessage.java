package net.pingfang.services.values;

import lombok.Builder;
import lombok.Data;
import net.pingfang.services.ServiceProduct;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 17:45
 */
@Data
@Builder(toBuilder = true)
public class ServiceMessage {
	final ServiceProduct product;
	final Long laneId;
	final Object object;
}
