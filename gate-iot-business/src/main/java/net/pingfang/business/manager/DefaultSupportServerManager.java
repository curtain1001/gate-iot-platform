package net.pingfang.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpSupportService;
import net.pingfang.business.service.IBtpSupportServiceService;
import net.pingfang.iot.common.SupportConfigure;
import net.pingfang.iot.common.manager.SupportServiceConfigManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 11:47
 */
@Component
public class DefaultSupportServerManager implements SupportServiceConfigManager {

	@Resource
	public IBtpSupportServiceService supportService;

	@Override
	public SupportConfigure getConfigure(Long laneId, String serviceCode) {
		LambdaQueryWrapper<BtpSupportService> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpSupportService::getLaneId, laneId);
		queryWrapper.eq(BtpSupportService::getServiceCode, serviceCode);
		BtpSupportService support = supportService.getOne(queryWrapper);
		return SupportConfigure.builder() //
				.laneId(laneId)//
				.deviceIds(support.getDeviceIds())//
				.configure(support.getConfiguration())//
				.serviceCode(serviceCode)//
				.build();
	}
}
