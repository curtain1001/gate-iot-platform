package net.pingfang.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-18 20:54
 */
public class ServiceProductSupports {
	private static final Map<String, ServiceProduct> all = new ConcurrentHashMap<>();

	public static void register(Collection<ServiceProduct> transport) {
		transport.forEach(ServiceProductSupports::register);
	}

	public static void register(ServiceProduct transport) {
		all.put(transport.name.toUpperCase(), transport);
	}

	public static List<ServiceProduct> get() {
		return new ArrayList<>(all.values());
	}

	public static Optional<ServiceProduct> lookup(String id) {
		return Optional.ofNullable(all.get(id.toUpperCase()));
	}

	public static List<ServiceProductSupports.Value> getServiceSupports() {
		return all.values().stream() //
				.map(x -> ServiceProductSupports.Value.builder() //
						.value(x.name())//
						.name(x.getName()) //
						.build())
				.collect(Collectors.toList());
	}

	@Data
	@Builder
	private static class Value {
		String name;
		String value;
		List<CustomizedSettingData> basicForm;
	}
}
