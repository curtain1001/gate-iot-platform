package net.pingfang.network.nova.server;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;
import net.pingfang.network.nova.client.NovaClientBasicFormCustomized;

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
public enum NovaServerNetworkType implements NetworkType {
	NOVA_SERVER("诺瓦LED服务端"),//
	;

	private final String name;
	static {
		NetworkTypes.register(Collections.singletonList(NOVA_SERVER));
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(NovaClientBasicFormCustomized.values());
	}

}
