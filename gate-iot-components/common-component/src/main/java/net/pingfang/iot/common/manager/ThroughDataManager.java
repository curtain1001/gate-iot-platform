package net.pingfang.iot.common.manager;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-28 16:36
 */
public interface ThroughDataManager {
	public JsonNode getData(Long laneNo);

	public void putData(JsonNode jsonNode);
}
