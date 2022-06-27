package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpNetworkConfig;
import net.pingfang.business.mapper.BtpNetworkConfigMapper;
import net.pingfang.business.service.IBtpNetworkConfigService;
import net.pingfang.network.NetworkConfigManager;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkType;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:46
 */
@Service
public class BtpNetworkConfigServiceImpl extends ServiceImpl<BtpNetworkConfigMapper, BtpNetworkConfig>
		implements IBtpNetworkConfigService, NetworkConfigManager {
	@Override
	public Mono<NetworkProperties> getConfig(NetworkType networkType, String id) {
		return Mono.just(getById(id)).map(BtpNetworkConfig::toNetworkProperties);
	}
}
