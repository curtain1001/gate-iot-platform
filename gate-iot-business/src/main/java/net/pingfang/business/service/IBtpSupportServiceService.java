package net.pingfang.business.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpSupportService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 10:54
 */
public interface IBtpSupportServiceService extends IService<BtpSupportService> {

	/**
	 * 服务添加设备
	 *
	 * @param laneId      车道id
	 * @param deviceId    设备主键id
	 * @param serviceCode 服务代码
	 * @return
	 */
	boolean addDevice(long laneId, long deviceId, String serviceCode);
}
