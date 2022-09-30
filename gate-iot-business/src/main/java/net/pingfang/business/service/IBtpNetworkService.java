package net.pingfang.business.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpNetwork;
import net.pingfang.iot.common.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:45
 */
public interface IBtpNetworkService extends IService<BtpNetwork> {
	String getNetworkNameByDevice(String deviceName, NetworkType networkType);
}
