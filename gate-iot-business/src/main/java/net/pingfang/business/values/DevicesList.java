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
 * @since 2022-12-21 10:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicesList {
	private boolean activated;
	private String deviceIp;
	private String deviceName;
	private String deviceState;
	private String deviceType;
	private String message;
}
