package net.pingfang.iot.common.customizedsetting.repos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;

import lombok.AllArgsConstructor;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.customizedsetting.Customized;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.customizedsetting.values.Options;

@AllArgsConstructor
public class CustomizedSettingRepository {

	public static List<CustomizedSettingData> getValues(Customized[] customized) {
		return Arrays.stream(customized).map(x -> {
			return CustomizedSettingData.builder() //
					.customizeType(x.getCustomizeType())//
					.attribute(x) //
					.options(StringUtils.isNotEmpty(x.getOptions()) ? JsonUtils.toArray(x.getOptions(), Options.class)
							: x.getOptions())//
					.label(x.getLabel())//
					.value(x.getValue())//
					.type(x.getType()) //
					.defaults(x.getDefaults())//
					.build();
		}).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		Options options1 = Options.builder().key("手动触发").value("shoudaong").build();
		Options options2 = Options.builder().key("手动触发").value("shoudaong").build();
		List<Options> d = Lists.newArrayList();
		d.add(options1);
		d.add(options2);
		String str = "[{\"key\":\"手动触发\",\"value\":\"shoudaong\"},{\"key\":\"手动触发\",\"value\":\"shoudaong\"}]";
		System.out.println(JsonUtils.toJsonString(d));
		System.out.println(JsonUtils.toJsonString(JsonUtils.toArray(str, Options.class)));
	}
}
