package net.pingfang.network.tcp.server;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;
import net.pingfang.network.tcp.client.TcpClientBasicForm;

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
public enum TcpServerNetworkType implements NetworkType {
	TCP_SERVER("TCP服务端"),//
	;

	private final String name;

	@Override
	public String getId() {
		return name();
	}

	static {
		NetworkTypes.register(Collections.singletonList(TcpServerNetworkType.TCP_SERVER));
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(TcpClientBasicForm.values());
	}

}
