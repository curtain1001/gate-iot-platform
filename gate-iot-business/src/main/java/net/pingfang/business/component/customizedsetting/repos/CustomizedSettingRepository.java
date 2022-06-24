package net.pingfang.business.component.customizedsetting.repos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Lists;

import lombok.AllArgsConstructor;
import net.pingfang.business.component.customizedsetting.values.CustomizedSettingData;
import net.pingfang.business.component.customizedsetting.values.DefaultCustomized;
import net.pingfang.business.component.customizedsetting.values.Options;
import net.pingfang.common.utils.JsonUtils;

@AllArgsConstructor
public class CustomizedSettingRepository {

	public static List<CustomizedSettingData> getValues() {
		return Arrays.stream(DefaultCustomized.values()).map(x -> {
			return CustomizedSettingData.builder() //
					.customizeType(x.getCustomizeType())//
					.attribute(x) //
					.options("select".equals(x.getType()) ? JsonUtils.toArray(x.getOptions(), Options.class)
							: x.getOptions())//
					.label(x.getLabel())//
					.value(x.getValue())//
					.type(x.getType()) //
					.build();
		}).collect(Collectors.toList());
	}

//	public static CustomizedSettingData covert(String customized) {
//		if (StringUtils.isEmpty(customized)) {
//			return null;
//		}
//		DefaultCustomized defaultCustomized = DefaultCustomized.valueOf(customized);
//	}
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
