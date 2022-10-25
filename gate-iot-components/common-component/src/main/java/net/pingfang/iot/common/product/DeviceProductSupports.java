package net.pingfang.iot.common.product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description 设备支持
 * @date 2022-06-30 22:00
 */

public class DeviceProductSupports {
	private static final Map<String, DeviceProduct> all = new ConcurrentHashMap<>();

	public static void register(Collection<DeviceProduct> transport) {
		transport.forEach(DeviceProductSupports::register);
	}

	public static void register(DeviceProduct transport) {
		all.put(transport.getValue().toUpperCase(), transport);
	}

	public static List<DeviceProduct> get() {
		return new ArrayList<>(all.values());
	}

	public static Optional<DeviceProduct> lookup(String id) {
		return Optional.ofNullable(all.get(id.toUpperCase()));
	}

	public static List<Value> getDeviceSupports() {
		return all.values().stream() //
				.map(x -> Value.builder() //
						.value(x.toString())//
						.name(x.getName()) //
						.build())
				.collect(Collectors.toList());
	}

	@Data
	@Builder
	private static class Value {
		String name;
		String value;
	}

}
