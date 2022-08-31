package net.pingfang.iot.common.instruction;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-23 11:05
 */
public enum FormatType {

	JSON, STRING, BINARY, HEX, SCRIPT, UNKNOWN;

	public static FormatType of(String of) {
		for (FormatType value : FormatType.values()) {
			if (value.name().equalsIgnoreCase(of)) {
				return value;
			}
		}
		return UNKNOWN;
	}

}
