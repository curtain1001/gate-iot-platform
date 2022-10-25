package net.pingfang.device.plc;

import java.util.List;

import com.google.common.collect.Lists;

import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.iot.common.product.DeviceProductSupports;
import net.pingfang.network.tcp.client.TcpClientNetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:33
 */
public enum PLCDeviceProduct implements DeviceProduct {
	PLC("PLC逻辑控制器");

	private final String name;
	static {
		DeviceProductSupports.register(PLC);
	}

	PLCDeviceProduct(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return this.name();
	}

	@Override
	public List<NetworkType> getNetwork() {
		return Lists.newArrayList(TcpClientNetworkType.TCP_CLIENT);
	}

}
