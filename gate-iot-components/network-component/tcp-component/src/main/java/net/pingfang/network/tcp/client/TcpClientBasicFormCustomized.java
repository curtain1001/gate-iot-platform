package net.pingfang.network.tcp.client;

import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.customizedsetting.Customized;
import net.pingfang.network.tcp.parser.CustomizedOptions;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-26 9:43
 */
public enum TcpClientBasicFormCustomized implements Customized {
	IP("host", "string", "", "ip地址", "", ""), //
	PORT("port", "number", "", "端口号", "", ""), //
	KEEPALIVE("keepalive", "boolean", "", "长连接", "", "false"), //
	PARSER_TYPE("parserType", "customized", CustomizedOptions.parserTypeOptions(), "解析方式", "", ""), //
	;

	TcpClientBasicFormCustomized(String value, String type, String options, String label, String customizeType,
			Object defaults) {
		this.value = value;
		this.type = type;
		// 对options进行赋值
		if (StringUtils.isEmpty(options) && "boolean".equals(this.type)) {
			this.options = booleanOptions();
		} else {
			this.options = options;
		}
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

	private String booleanOptions() {
		return "[{\"key\": \"是\",\"value\": \"1\"},{\"key\": \"否\",\"value\": \"0\"}]";
	}

}
