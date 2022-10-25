package net.pingfang.iot.common.manager;

import net.pingfang.iot.common.SupportConfigure;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 11:03
 */
public interface SupportServiceConfigManager {
	SupportConfigure getConfigure(Long laneId, String serviceCode);
}
