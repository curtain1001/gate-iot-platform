package net.pingfang.device.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description 设备产品类型
 * @date 2022-06-30 21:55
 */
public enum Product {
	PLC("PLC"), //
	OCR_License_Plate("第三代车牌识别相机"), //
	LED("LED"), //
	;//

	public final String name;

	Product(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<ProductObject> getValues() {
		return Arrays.stream(values()).map(x -> ProductObject.builder().name(x.getName()).value(x.name()).build())
				.collect(Collectors.toList());
	}

	@Data
	@Builder(toBuilder = true)
	static class ProductObject {
		final String name;
		final String value;
	}
}
