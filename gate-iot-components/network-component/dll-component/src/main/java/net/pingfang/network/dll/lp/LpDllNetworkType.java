package net.pingfang.network.dll.lp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-18 14:41
 */
@AllArgsConstructor
@Getter
public enum LpDllNetworkType implements NetworkType {
	LP_DLL("识别相机客户端3.0"),//
	;

	private final String name;
	static {
		NetworkTypes.register(LP_DLL);
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(LpBasicForm.values());
	}

}
