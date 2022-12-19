package net.pingfang.business.device;

import lombok.Getter;
import net.pingfang.common.customizedsetting.Customized;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-26 11:42
 */
@Getter
public enum NovaBasicForm implements Customized {
	IP("host", "string", "", "ip地址", "", ""), //
	PORT("port", "number", "", "端口号", "", ""), //
	SERVER("server", "boolean", "", "服务端方式", "", ""), //
	DWELL_TIME("dwellTime", "number", "", "停留时间", "", ""), //
	START_DISPLAY_MODE("startDisplayMode", "number", "", "入屏方式", "", ""), //
	END_DISPLAY_MODE("endDisplayMode", "number", "", "出屏方式", "", ""), //
	START_SPEED("startSpeed", "number", "", "入屏速度", "", ""), //
	FLICKER_SPEED("flickerSpeed", "number", "", "闪烁速度", "", ""), //
	DISPLAY_WIDTH("displayWidth", "number", "", "显示区域宽度", "", ""), //
	DISPLAY_HIGH("displayHigh", "number", "", "显示区域高度", "", ""), //
	;

	NovaBasicForm(String value, String type, String options, String label, String customizeType, Object defaults) {
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
