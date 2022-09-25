package net.pingfang.iot.common.customizedsetting.values;

import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.customizedsetting.Customized;

public enum DefaultCustomized implements Customized {

	TRIGGER_MODE("TRIGGER_MODE", "select", "", "触发方式", "", ""), //
	WORK_UPLOAD_URL("WORK_UPLOAD_URL", "string", "", "采集数据上传地址", "", ""), //
	PICTURE_STORE_DIRECTORY("PICTURE_STORE_DIRECTORY", "string", "", "图片保存地址", "", ""), //
	PICTURE_IS_UPLOAD("PICTURE_IS_UPLOAD", "boolean", "", "图片是否上传", "", ""), //
	PICTURE_UPLOAD_OVERTIME("PICTURE_IS_UPLOAD", "number", "", "图片上传超时时间(单位：毫秒)", "", ""), //
	PICTURE_UPLOAD_METHOD("PICTURE_UPLOAD_METHOD", "select", "", "图片上传方式", "", ""), //

	VIDEO_STORE_DIRECTORY("VIDEO_STORE_DIRECTORY", "string", "", "视频保存地址", "", ""), //
	VIDEO_IS_UPLOAD("VIDEO_IS_UPLOAD", "boolean", "", "视频是否上传", "", ""), //
	VIDEO_UPLOAD_OVERTIME("VIDEO_UPLOAD_OVERTIME", "number", "", "视频上传超时时间(单位：毫秒)", "", ""), //
	VIDEO_UPLOAD_METHOD("VIDEO_UPLOAD_METHOD", "string", "", "视频上传方式", "", ""), //
	EQUIPMENT_CONTROL_URL("VIDEO_UPLOAD_METHOD", "string", "", "服务设备远控地址（被调用）", "", ""), //
	EQUIPMENT_STATE_IS_UPLOAD("EQUIPMENT_STATE_IS_UPLOAD", "boolean", "", "设备状态报文是否上传", "", ""), //
	EQUIPMENT_STATE_UPLOAD_URL("EQUIPMENT_STATE_UPLOAD_URL", "string", "", "设备状态报文上传地址", "", ""), //
	UPLOAD_INTERVAL_SECOND("UPLOAD_INTERVAL_SECOND", "number", "", "设备状态报文上传间隔秒（毫秒）", "", ""), //
	WORKING_SAVE_DAY("WORKING_SAVE_DAY", "number", "", "过车记录保存时间（天）", "", ""), //
	LOG_SAVE_DAY("LOG_SAVE_DAY", "number", "", "日志保存时间（天）", "", ""), //
	LANE_IP("LANE_IP", "string", "", "车道IP地址", "", ""),//
	;

	DefaultCustomized(String value, String type, String options, String label, String customizeType, Object defaults) {
		this.value = value;
		this.type = type;
		// 对options进行赋值
		if (StringUtils.isEmpty(options)) {
			if ("boolean".equals(this.type)) {
				options = booleanOptions();
			}
			if ("select".equals(this.type) && "TRIGGER_MODE".equals(value)) {
				options = selectOptionsOfTriggerMode();
			}
			if ("select".equals(this.type) && "PICTURE_UPLOAD_METHOD".equals(value)) {
				options = selectOptionsOfPictureUploadMethod();
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

	private String selectOptionsOfTriggerMode() {
		return "[{\"key\": \"手动触发\",\"value\": \"shoudaong\"},{\"key\": \"自动触发\",\"value\": \"zidong\"}]";
	}

	private String selectOptionsOfPictureUploadMethod() {
		return "[{\"key\": \"TCP\",\"value\": \"TCP\"},{\"key\": \"FTP\",\"value\": \"FTP\"}]";
	}
}
