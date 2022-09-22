package net.pingfang.business.eventhandlers;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.eventbus.Subscribe;

import net.pingfang.business.domain.BtpNetworkConfig;
import net.pingfang.business.enums.NetworkConfigState;
import net.pingfang.business.events.ServerNetworkCreatedEvent;
import net.pingfang.business.events.ServerNetworkDeleteEvent;
import net.pingfang.business.events.ServerNetworkUpdateEvent;
import net.pingfang.business.service.IBtpNetworkConfigService;
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
	public IBtpNetworkConfigService configService;

	@Subscribe
	public void on(ServerNetworkCreatedEvent event) {
		BtpNetworkConfig config = BtpNetworkConfig.builder()//
				.networkConfigId(event.getId())//
				.name(event.getName())//
				.control(event.getControl())//
				.configuration(event.getConfigurations())//
				.enabled(event.getEnabled())//
				.type(event.getType())//
				.status(NetworkConfigState.disabled)//
				.createBy(event.getCreateBy())//
				.createTime(event.getCreateTime())//
				.build();
		configService.save(config);
	}

	@Subscribe
	public void on(ServerNetworkUpdateEvent event) {
		LambdaQueryWrapper<BtpNetworkConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpNetworkConfig::getNetworkConfigId, event.getId());
		BtpNetworkConfig config = configService.getOne(queryWrapper);
		if (config != null) {
			config = config.toBuilder() //
					.name(event.getName())//
					.control(event.getControl())//
					.configuration(event.getConfigurations())//
					.enabled(event.getEnabled())//
					.type(event.getType())//
					.updateBy(event.getUpdateBy())//
					.updateTime(event.getUpdateTime())//
					.build();
			configService.updateById(config);
		}

	}

	@Subscribe
	public void on(ServerNetworkDeleteEvent event) {
		LambdaUpdateWrapper<BtpNetworkConfig> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(BtpNetworkConfig::getNetworkConfigId, event.getId());
		configService.remove(updateWrapper);
	}
}
