package net.pingfang.network.http.server;

import java.util.Collections;
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
public enum HttpServerNetworkType implements NetworkType {
	HTTP_SERVER("HTTP服务端"),//
	;

	private final String name;
	static {
		NetworkTypes.register(Collections.singletonList(HTTP_SERVER));
	}

	@Override
	public String getId() {
		return name();
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(HttpServerBasicFormCustomized.values());
	}

}
