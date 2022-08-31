package net.pingfang.iot.common.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkTypes {

	private static final Map<String, NetworkType> all = new ConcurrentHashMap<>();

	public static void register(Collection<NetworkType> transport) {
		transport.forEach(NetworkTypes::register);
	}

	public static void register(NetworkType transport) {
		all.put(transport.getId().toUpperCase(), transport);
	}

	public static List<NetworkType> get() {
		return new ArrayList<>(all.values());
	}

	public static Optional<NetworkType> lookup(String id) {
		return Optional.ofNullable(all.get(id.toUpperCase()));
	}

}
