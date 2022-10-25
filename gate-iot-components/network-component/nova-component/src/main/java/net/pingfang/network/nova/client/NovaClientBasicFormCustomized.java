package net.pingfang.network.nova.client;

import lombok.Getter;
import net.pingfang.iot.common.customizedsetting.Customized;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-26 11:42
 */
@Getter
public enum NovaClientBasicFormCustomized implements Customized {
	IP("host", "string", "", "ip地址", "", ""), //
	PORT("port", "number", "", "端口号", "", ""), //
	;

	NovaClientBasicFormCustomized(String value, String type, String options, String label, String customizeType,
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

}
