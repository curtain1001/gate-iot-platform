package net.pingfang.business.values;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 1:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Driver {
	private String driverAddress;
	private long driverIdentityCard;
	private String driverName;
	private double driverTemperature;
}
