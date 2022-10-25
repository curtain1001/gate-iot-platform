package net.pingfang.business.eventhandlers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.Subscribe;

import net.pingfang.business.domain.BtpNetwork;
import net.pingfang.business.enums.NetworkState;
import net.pingfang.business.events.ServerNetworkCreatedEvent;
import net.pingfang.business.events.ServerNetworkDeleteEvent;
import net.pingfang.business.events.ServerNetworkUpdateEvent;
import net.pingfang.business.service.IBtpNetworkService;
import net.pingfang.common.event.EventBusListener;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-19 14:31
 */
@EventBusListener
@Component
public class NetworkHandler {
	@Resource
	public IBtpNetworkService networkService;

	@Subscribe
	public void on(ServerNetworkCreatedEvent event) {
		BtpNetwork config = BtpNetwork.builder()//
				.networkId(event.getId())//
				.name(event.getName())//
				.configuration(event.getConfigurations())//
				.enabled(event.isEnabled())//
				.type(event.getType())//
				.status(NetworkState.disabled)//
				.createBy(event.getCreateBy())//
				.createTime(event.getCreateTime())//
				.build();
		networkService.save(config);
	}

	@Subscribe
	public void on(ServerNetworkUpdateEvent event) {
		LambdaQueryWrapper<BtpNetwork> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpNetwork::getNetworkId, event.getId());
		BtpNetwork config = networkService.getOne(queryWrapper);
		if (config != null) {
			config = config.toBuilder() //
					.name(event.getName())//
					.configuration(event.getConfigurations())//
					.enabled(event.isEnabled())//
					.type(event.getType())//
					.updateBy(event.getUpdateBy())//
					.updateTime(event.getUpdateTime())//
					.build();
			networkService.updateById(config);
		}

	}

	@Subscribe
	public void on(ServerNetworkDeleteEvent event) {
		LambdaUpdateWrapper<BtpNetwork> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(BtpNetwork::getNetworkId, event.getId());
		networkService.remove(updateWrapper);
	}
}
