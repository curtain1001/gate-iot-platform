package net.pingfang.business.device;

import lombok.Getter;
import net.pingfang.common.customizedsetting.Customized;
import net.pingfang.common.utils.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-27 8:56
 */
@Getter
public enum HikBasicForm implements Customized {
	IP("host", "string", "", "ip地址", "", ""), //
	PORT("port", "number", "", "端口号", "", ""), //
	USERNAME("username", "string", "", "用户名", "", ""), //
	PASSWORD("password", "string", "", "密码", "", ""), //
	;

	HikBasicForm(String value, String type, String options, String label, String customizeType, Object defaults) {
		this.value = value;
		this.type = type;
		// 对options进行赋值
		if (StringUtils.isEmpty(options)) {
			if ("boolean".equals(this.type)) {
				options = booleanOptions();
			}
		}
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

	private String booleanOptions() {
		return "[{\"key\": \"是\",\"value\": \"1\"},{\"key\": \"否\",\"value\": \"0\"}]";
	}
}
