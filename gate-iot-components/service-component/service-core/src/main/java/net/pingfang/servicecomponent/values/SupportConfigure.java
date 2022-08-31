package net.pingfang.servicecomponent.values;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 11:33
 */
@Data
@Builder(toBuilder = true)
public class SupportConfigure {
	private String networkId;
}
