package net.pingfang.network.http.server;

import net.pingfang.iot.common.customizedsetting.Customized;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-26 11:42
 */
public enum HttpServerBasicFormCustomized implements Customized {
	IP("host", "string", "", "ip地址", "", ""), //
	PORT("port", "number", "", "端口号", "", ""), //
	;

	HttpServerBasicFormCustomized(String value, String type, String options, String label, String customizeType,
			Object defaults) {
		this.value = value;
		this.type = type;
		this.options = options;
		this.label = label;
		this.customizeType = customizeType;
		this.defaults = defaults;
	}

	private final String value;
	private final String type;
	private final String options;
	private final String label;
	private final String customizeType;// 自定义大类
	private final Object defaults;

	public String getValue() {
		return this.value;
	}

	public String getLabel() {
		return this.label;
	}

	public String getType() {
		return type;
	}

	public String getCustomizeType() {
		return customizeType;
	}

	public Object getDefaults() {
		return defaults;
	}

	public String getOptions() {
		return options;
	}

}
