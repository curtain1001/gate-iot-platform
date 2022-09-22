package net.pingfang.business.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpServer;
import net.pingfang.servicecomponent.core.ServerProperties;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:00
 */
public interface IBtpServerService extends IService<BtpServer> {
	public ServerProperties getProperties(String serverId);

	boolean updateById(BtpServer server);

	boolean save(BtpServer server);
}
