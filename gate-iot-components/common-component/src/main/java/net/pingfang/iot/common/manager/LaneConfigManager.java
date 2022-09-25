package net.pingfang.iot.common.manager;

import java.util.Map;

import net.pingfang.iot.common.customizedsetting.Customized;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 16:41
 */
public interface LaneConfigManager {
	Map<Customized, Object> getConfig(Long laneId);

	String getConfig(Customized value, Long laneId);

	Map<Long, Object> getConfig(Customized value);

}
