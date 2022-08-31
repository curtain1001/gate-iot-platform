package net.pingfang.network;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.pingfang.iot.common.network.NetworkType;

@Getter
@Setter
public class NetworkProperties implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String name;

	private boolean enabled;

	private Control control;

	private NetworkType networkType;

	private Map<String, Object> configurations;

}
