package net.pingfang.business.device;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Data;
import net.pingfang.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.gate.protocol.values.DeviceProduct;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-09 16:46
 */
public class BasicFormSupport {
	private static final Map<DeviceProduct, List<CustomizedSettingData>> basicForm = Maps.newHashMap();
	static {
		basicForm.put(DeviceProduct.HIKVISION, CustomizedSettingRepository.getValues(HikBasicForm.values()));
		basicForm.put(DeviceProduct.PLC, CustomizedSettingRepository.getValues(PLCBasicForm.values()));
		basicForm.put(DeviceProduct.NOVA_LED, CustomizedSettingRepository.getValues(NovaBasicForm.values()));
		basicForm.put(DeviceProduct.OCR_LICENSE_PLATE_III, CustomizedSettingRepository.getValues(LpBasicForm.values()));
	}

	public static List<FormInfo> getForms() {
		return basicForm.entrySet().stream().map(x -> FormInfo.of(x.getKey(), x.getValue()))
				.collect(Collectors.toList());
	}

	public static List<BasicFormSupport.Value> getSupports() {
		return basicForm.keySet().stream() //
				.map(x -> BasicFormSupport.Value.builder() //
						.value(x.toString())//
						.name(x.getName()) //
						.build())
				.collect(Collectors.toList());
	}

	@Data
	@Builder
	private static class Value {
		String name;
		String value;
	}
}
