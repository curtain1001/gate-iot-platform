package net.pingfang.wheelhubocr;

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
 * @since 2022-08-30 11:29
 */
public enum WheelHubOcrProduct implements Product {
	WHEEL_HUB_OCR;

	{
		ProductSupports.registry(this);
	}

	@Override
	public String getName() {
		return "车轮毂识别";
	}

	@Override
	public String getValue() {
		return WheelHubOcrProduct.WHEEL_HUB_OCR.name();
	}

	@Override
	public NetworkType getNetwork() {
		return DefaultNetworkType.TCP_SERVER;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(WheelHubOcrBasicFormCustomized.values());
	}

	@Override
	public ObjectType getType() {
		return ObjectType.service;
	}

}
