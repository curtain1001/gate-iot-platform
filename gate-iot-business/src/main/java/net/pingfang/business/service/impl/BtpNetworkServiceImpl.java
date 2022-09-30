package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpNetwork;
import net.pingfang.business.mapper.BtpNetworkMapper;
import net.pingfang.business.service.IBtpNetworkService;
import net.pingfang.iot.common.network.NetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:46
 */
@Service
public class BtpNetworkServiceImpl extends ServiceImpl<BtpNetworkMapper, BtpNetwork> implements IBtpNetworkService {
	@Override
	public String getNetworkNameByDevice(String deviceName, NetworkType networkType) {
		return deviceName + "::" + networkType.getName();
	}
}
