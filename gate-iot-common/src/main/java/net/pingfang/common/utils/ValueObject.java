package net.pingfang.common.utils;

import java.util.Map;
import java.util.Optional;

import org.springframework.util.StringUtils;

public interface ValueObject {

	Map<String, Object> values();

	default Optional<Object> get(String name) {
		return Optional.ofNullable(values()).map(map -> map.get(name));
	}

	default Optional<Integer> getInt(String name) {
		return get(name, Integer.class);
	}

	default int getInt(String name, int defaultValue) {
		return getInt(name).orElse(defaultValue);
	}

	default Optional<Long> getLong(String name) {
		return get(name, Long.class);
	}

	default long getLong(String name, long defaultValue) {
		return getLong(name).orElse(defaultValue);
	}

//	default Optional<Duration> getDuration(String name) {
//		return getString(name).map(TimeUtils::parse);
//	}
//
//	default Optional<Interval> getInterval(String name) {
//		return getString(name).map(Interval::of);
//	}
//
//	default Interval getInterval(String name, Interval defaultValue) {
//		return getString(name).map(Interval::of).orElse(defaultValue);
//	}
//
//	default Duration getDuration(String name, Duration defaultValue) {
//		return getDuration(name).orElse(defaultValue);
//	}
//
//	default Optional<Date> getDate(String name) {
//		return get(name).map(String::valueOf).map(TimeUtils::parseDate);
//	}
//
//	default Date getDate(String name, Date defaultValue) {
//		return getDate(name).orElse(defaultValue);
//	}

	default Optional<Double> getDouble(String name) {
		return get(name, Double.class);
	}

	default double getDouble(String name, double defaultValue) {
		return getDouble(name).orElse(defaultValue);
	}

	default Optional<String> getString(String name) {
		return get(name, String.class).filter(StringUtils::hasText);
	}

	default String getString(String name, String defaultValue) {
		return getString(name).orElse(defaultValue);
	}

	default Optional<Boolean> getBoolean(String name) {
		return get(name, Boolean.class);
	}

	default boolean getBoolean(String name, boolean defaultValue) {
		return getBoolean(name).orElse(defaultValue);
	}

	default <T> Optional<T> get(String name, Class<T> type) {
		return get(name).map(obj -> JsonUtils.toObject(JsonUtils.toJsonBytes(obj), type));
	}

	static ValueObject of(Map<String, Object> mapVal) {
		return () -> mapVal;
	}

	default <T> T as(Class<T> type) {
//		return FastBeanCopier.copy(values(), type);
		return null;
	}
}
