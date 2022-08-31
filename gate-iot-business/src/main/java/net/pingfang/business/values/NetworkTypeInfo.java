package net.pingfang.business.values;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetworkTypeInfo {

	private String id;

	private String name;

	private List<CustomizedSettingData> basicForm;

	public static NetworkTypeInfo of(NetworkType type) {

		NetworkTypeInfo info = new NetworkTypeInfo();

		info.setId(type.getId());
		info.setName(type.getName());

		return info;

	}

	public static NetworkTypeInfo of(NetworkType type, List<CustomizedSettingData> data) {

		NetworkTypeInfo info = new NetworkTypeInfo();

		info.setId(type.getId());
		info.setName(type.getName());
		info.setBasicForm(data);
		return info;

	}

}
