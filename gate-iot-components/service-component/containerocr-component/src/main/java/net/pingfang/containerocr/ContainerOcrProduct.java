package net.pingfang.containerocr;

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
public enum ContainerOcrProduct implements Product {
	CONTAINER_OCR;

	{
		ProductSupports.registry(this);
	}

	@Override
	public String getName() {
		return "箱号识别";
	}

	@Override
	public String getValue() {
		return "CONTAINER_OCR";
	}

	@Override
	public NetworkType getNetwork() {
		return DefaultNetworkType.TCP_SERVER;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(ContainerOcrBasicFormCustomized.values());
	}

	@Override
	public ObjectType getType() {
		return ObjectType.service;
	}

}
