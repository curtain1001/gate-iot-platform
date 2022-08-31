package net.pingfang.iot.common.product;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Component;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description 设备支持
 * @date 2022-06-30 22:00
 */

@Component
public class ProductSupports {
	final static List<Product> supports = Lists.newArrayList();

	public static void registry(Product product) {
		supports.add(product);
	}

	public static Product getSupport(String value) {
		return supports.stream().filter(x -> x.isSupported(value)).findFirst().orElse(null);
	}

	public static List<Value> getDeviceSupports() {
		return supports.stream().filter(x -> ObjectType.device.equals(x.getType())) //
				.map(x -> Value.builder() //
						.value(x.toString())//
						.name(x.getName()) //
						.basicForm(x.getBasicForm()) //
						.build())
				.collect(Collectors.toList());
	}

	public static List<Value> getServerSupports() {
		return supports.stream().filter(x -> ObjectType.service.equals(x.getType())) //
				.map(x -> Value.builder() //
						.value(x.toString())//
						.name(x.getName()) //
						.basicForm(x.getBasicForm()) //
						.build())
				.collect(Collectors.toList());
	}

	@Data
	@Builder
	private static class Value {
		String name;
		String value;
		List<CustomizedSettingData> basicForm;
	}

}
