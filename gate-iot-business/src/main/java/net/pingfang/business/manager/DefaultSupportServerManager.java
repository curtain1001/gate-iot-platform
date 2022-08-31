package net.pingfang.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpSupportServer;
import net.pingfang.business.service.IBtpSupportServerService;
import net.pingfang.servicecomponent.core.SupportServerManager;
import net.pingfang.servicecomponent.values.SupportConfigure;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 11:47
 */
@Component
public class DefaultSupportServerManager implements SupportServerManager {
	@Resource
	public IBtpSupportServerService supportServerService;

	@Override
	public SupportConfigure getConfigure(String product) {
		LambdaQueryWrapper<BtpSupportServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpSupportServer::getProduct, product);
		BtpSupportServer supportServer = supportServerService.getOne(queryWrapper);
		return SupportConfigure.builder() //
				.networkId(supportServer.getNetworkId())//
				.build();
	}
}
