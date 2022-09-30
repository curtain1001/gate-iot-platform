package net.pingfang.iot.common.manager;

import net.pingfang.iot.common.NetworkSession;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-22 16:39
 */
public interface SessionManager {

	void register(String id, NetworkSession session);

	void shutdown(String id);
}
