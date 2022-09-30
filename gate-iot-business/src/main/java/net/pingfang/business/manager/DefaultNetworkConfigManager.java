package net.pingfang.business.manager;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpNetwork;
import net.pingfang.business.enums.NetworkState;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpNetworkService;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.NetworkConfigManager;
import net.pingfang.network.NetworkProperties;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 11:00
 */
@Component
public class DefaultNetworkConfigManager implements NetworkConfigManager {
	@Resource
	public IBtpNetworkService networkService;

	@Resource
	public IBtpDeviceService deviceService;

	@Override
	public NetworkProperties getConfig(NetworkType networkType, String id) {
		LambdaQueryWrapper<BtpDevice> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(BtpDevice::getDeviceId, id);
		return deviceService.getOne(lambdaQueryWrapper).toNetworkProperties();
	}

	@Override
	public NetworkProperties getConfig(String configId) {
		LambdaQueryWrapper<BtpNetwork> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpNetwork::getNetworkId, configId);
		BtpNetwork config = networkService.getOne(queryWrapper);
		if (config != null) {
			return config.toNetworkProperties();
		}
		return new NetworkProperties();
	}

	@Override
	public List<NetworkProperties> getConfig() {
		List<BtpNetwork> configs = networkService.list();
		if (!configs.isEmpty()) {
			return configs.stream().map(BtpNetwork::toNetworkProperties).collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}

	@Override
	public void update(String configId, String state) {
		LambdaQueryWrapper<BtpNetwork> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpNetwork::getNetworkId, configId);
		BtpNetwork config = networkService.getOne(queryWrapper);

		config = config.toBuilder() //
				.status(NetworkState.valueOf(state)) //
				.build();
		networkService.updateById(config);
	}
}
