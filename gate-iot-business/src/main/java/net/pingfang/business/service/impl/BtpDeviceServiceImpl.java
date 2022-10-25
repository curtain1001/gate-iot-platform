package net.pingfang.business.service.impl;

import static net.pingfang.common.utils.SecurityUtils.getUsername;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.mapper.BtpDeviceMapper;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.values.DeviceStatus;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.common.manager.AsyncManager;
import net.pingfang.iot.common.network.NetworkTypes;
import net.pingfang.network.Network;
import net.pingfang.network.NetworkManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:02
 */
@Service
public class BtpDeviceServiceImpl extends ServiceImpl<BtpDeviceMapper, BtpDevice> implements IBtpDeviceService {

	@Resource
	public BtpDeviceMapper deviceMapper;

	@Resource
	private NetworkManager networkManager;

	@Resource
	EventBusCenter busCenter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(BtpDevice device) {
		device.setStatus(DeviceStatus.START);
		int count = deviceMapper.insert(device);
		if (count > 0) {
			try {
				Network network = networkManager.getNetwork(NetworkTypes.lookup(device.getNetworkType()).get(),
						device.getDeviceId());

				AsyncManager.me().execute(new TimerTask() {
					@Override
					public void run() {
						if (network.isAlive()) {
							device.setStatus(DeviceStatus.ONLINE);
						} else {
							device.setStatus(DeviceStatus.OFFLINE);
						}
						deviceMapper.updateById(device);
					}
				}, 5L, TimeUnit.SECONDS);
			} catch (Exception e) {
				log.error("设备启动失败：", e);
				device.setUpdateBy(getUsername());
				device.setUpdateTime(new Date());
				device.setStatus(DeviceStatus.OFFLINE);
				deviceMapper.updateById(device);
			}
			return true;
		} else {
			return false;
		}
	}
}
