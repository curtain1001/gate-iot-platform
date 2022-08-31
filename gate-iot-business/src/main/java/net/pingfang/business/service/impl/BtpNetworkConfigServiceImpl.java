package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpNetworkConfig;
import net.pingfang.business.mapper.BtpNetworkConfigMapper;
import net.pingfang.business.service.IBtpNetworkConfigService;
import net.pingfang.iot.common.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:46
 */
@Service
public class BtpNetworkConfigServiceImpl extends ServiceImpl<BtpNetworkConfigMapper, BtpNetworkConfig>
		implements IBtpNetworkConfigService {
	@Override
	public String getNetworkNameByDevice(String deviceName, NetworkType networkType) {
		return deviceName + "::" + networkType.getName();
	}
}
