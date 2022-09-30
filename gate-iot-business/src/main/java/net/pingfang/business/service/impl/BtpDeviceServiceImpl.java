package net.pingfang.business.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.events.ServerNetworkCreatedEvent;
import net.pingfang.business.events.ServerNetworkDeleteEvent;
import net.pingfang.business.events.ServerNetworkUpdateEvent;
import net.pingfang.business.mapper.BtpDeviceMapper;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.Control;

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
	EventBusCenter busCenter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(BtpDevice device) {
		int count = deviceMapper.insert(device);
		if (count > 0) {
			Product product = ProductSupports.getSupport(device.getProduct());
			if (product != null && product.getNetwork() != null) {
				ServerNetworkCreatedEvent event = ServerNetworkCreatedEvent.builder().id(device.getDeviceId())//
						.name(device.getDeviceName())//
						.configurations(device.getConfiguration())//
						.control(Control.own)//
						.type(ProductSupports.getSupport(device.getProduct()).getNetwork().getId()) //
						.enabled(device.getEnabled())//
						.createBy(device.getCreateBy()) //
						.createTime(device.getUpdateTime())//
						.build();
				busCenter.postSync(event);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(BtpDevice device) {
		int count = deviceMapper.updateById(device);
		if (count > 0) {
			Product product = ProductSupports.getSupport(device.getProduct());
			if (product != null && product.getNetwork() != null) {
				ServerNetworkUpdateEvent event = ServerNetworkUpdateEvent.builder() //
						.id(device.getDeviceId())//
						.name(device.getDeviceName())//
						.configurations(device.getConfiguration())//
						.control(Control.own)//
						.type(ProductSupports.getSupport(device.getProduct()).getNetwork().getId()) //
						.enabled(device.getEnabled())//
						.updateBy(device.getUpdateBy()) //
						.updateTime(device.getUpdateTime())//
						.build();
				busCenter.postSync(event);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean removeById(Long id) {
		BtpDevice btpDevice = deviceMapper.selectById(id);
		if (btpDevice == null) {
			throw new RuntimeException("删除失败：该数据不存在");
		}
		int count = deviceMapper.deleteById(id);
		if (count > 0) {
			busCenter.postSync(ServerNetworkDeleteEvent.builder() //
					.id(btpDevice.getDeviceId()) //
					.build());
		}
		return false;
	}
}
