package net.pingfang.business.device;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.gate.protocol.values.DeviceProduct;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormInfo {

	private String id;

	private String name;

	private List<CustomizedSettingData> basicForm;

	public static FormInfo of(DeviceProduct type, List<CustomizedSettingData> data) {
		FormInfo info = new FormInfo();
		info.setId(type.name);
		info.setName(type.getName());
		info.setBasicForm(data);
		return info;
	}

}
