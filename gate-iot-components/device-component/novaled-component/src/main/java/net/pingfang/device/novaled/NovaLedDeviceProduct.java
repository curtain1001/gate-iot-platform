package net.pingfang.device.novaled;

import java.util.List;

import com.google.common.collect.Lists;

import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.iot.common.product.DeviceProductSupports;
import net.pingfang.network.nova.client.NovaClientNetworkType;
import net.pingfang.network.nova.server.NovaServerNetworkType;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 15:59
 */
public enum NovaLedDeviceProduct implements DeviceProduct {
	NOVA_LED;

	static {
		DeviceProductSupports.register(NOVA_LED);
	}

	@Override
	public String getName() {
		return "诺瓦LED";
	}

	@Override
	public String getValue() {
		return name();
	}

	@Override
	public List<NetworkType> getNetwork() {
		return Lists.newArrayList(NovaClientNetworkType.NOVA_CLIENT, NovaServerNetworkType.NOVA_SERVER);
	}
}
