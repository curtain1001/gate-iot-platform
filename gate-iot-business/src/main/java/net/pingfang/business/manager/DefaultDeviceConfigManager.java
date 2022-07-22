package net.pingfang.business.manager;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.manager.DeviceConfigManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 14:23
 */
@Component
public class DefaultDeviceConfigManager implements DeviceConfigManager {
	@Resource
	private IBtpDeviceService deviceService;

	@Override
	public DeviceProperties getProperties(String deviceId) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getDeviceId, deviceId);
		BtpDevice device = deviceService.getOne(queryWrapper);
		if (device != null) {
			return device.toProperties();
		}
		return null;
	}

	@Override
	public List<DeviceProperties> getProperties() {
		List<BtpDevice> device = deviceService.list();
		if (device != null) {
			return device.stream().map(BtpDevice::toProperties).collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}
}
