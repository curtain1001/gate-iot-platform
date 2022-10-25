package net.pingfang.network.nova.client;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.network.nova.NovaProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 19:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NovaClientProperties extends NovaProperties {
	private String ip;
	private int port;
	private Long laneId;
}
