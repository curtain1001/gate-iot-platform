package net.pingfang.network.nova.server;

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
public class NovaServerProperties extends NovaProperties {
	private String id;
	private int port;
	private Long laneId;
}
