package net.pingfang.common.utils;

import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-16 16:14
 */
public class RuntimeUtils {
	public static <T> boolean isStartupFromJar() {
		String protocol = RuntimeUtils.class.getResource("").getProtocol();
		return Objects.equals(protocol, "jar");
	}
}
