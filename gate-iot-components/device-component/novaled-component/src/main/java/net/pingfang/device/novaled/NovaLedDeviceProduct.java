package net.pingfang.device.novaled;

import java.util.List;

import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.DefaultNetworkType;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 15:59
 */
public enum NovaLedDeviceProduct implements Product {
	NOVA_LED;

	{
		ProductSupports.registry(this);
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
	public NetworkType getNetwork() {
		return DefaultNetworkType.INTERNAL;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(NovaLedBasicFormCustomized.values());
	}

	@Override
	public ObjectType getType() {
		return ObjectType.device;
	}
}
