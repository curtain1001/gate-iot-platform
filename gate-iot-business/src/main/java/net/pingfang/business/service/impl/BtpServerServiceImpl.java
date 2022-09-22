package net.pingfang.business.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpServer;
import net.pingfang.business.events.ServerNetworkCreatedEvent;
import net.pingfang.business.events.ServerNetworkUpdateEvent;
import net.pingfang.business.mapper.BtpServerMapper;
import net.pingfang.business.service.IBtpServerService;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.Control;
import net.pingfang.servicecomponent.core.ServerProperties;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:02
 */
@Service
public class BtpServerServiceImpl extends ServiceImpl<BtpServerMapper, BtpServer> implements IBtpServerService {
	@Resource
	public BtpServerMapper serverMapper;
	@Resource
	public EventBusCenter busCenter;

	@Override
	public ServerProperties getProperties(String serverId) {
		LambdaQueryWrapper<BtpServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpServer::getServerId, serverId);
		BtpServer server = serverMapper.selectOne(queryWrapper);
		if (server != null) {
			return server.toProperties();
		}
		return null;
	}

	@Override
	public boolean save(BtpServer server) {
		int count = serverMapper.insert(server);
		if (count > 0) {
			Product product = ProductSupports.getSupport(server.getProduct());
			if (product != null && product.getNetwork() != null) {
				ServerNetworkCreatedEvent event = ServerNetworkCreatedEvent.builder().id(server.getServerId())//
						.name(server.getServerName())//
						.configurations(server.getConfiguration())//
						.control(Control.own)//
						.type(ProductSupports.getSupport(server.getProduct()).getNetwork().getId()) //
						.enabled(server.getEnabled())//
						.createBy(server.getCreateBy()) //
						.createTime(server.getUpdateTime())//
						.build();
				busCenter.postSync(event);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateById(BtpServer server) {
		int count = serverMapper.updateById(server);
		if (count > 0) {
			Product product = ProductSupports.getSupport(server.getProduct());
			if (product != null && product.getNetwork() != null) {
				ServerNetworkUpdateEvent event = ServerNetworkUpdateEvent.builder() //
						.id(server.getServerId())//
						.name(server.getServerName())//
						.configurations(server.getConfiguration())//
						.control(Control.own)//
						.type(ProductSupports.getSupport(server.getProduct()).getNetwork().getId()) //
						.enabled(server.getEnabled())//
						.updateBy(server.getUpdateBy()) //
						.updateTime(server.getUpdateTime())//
						.build();
				busCenter.postSync(event);
			}
			return true;
		} else {
			return false;
		}

	}
}
