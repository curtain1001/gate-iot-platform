package net.pingfang.business.component.customizedsetting.values;

public enum AttributeEnum {

	TRIGGER_MODE("TRIGGER_MODE", "select", "", "触发方式", ""), //
	WORK_UPLOAD_URL("WORK_UPLOAD_URL", "string", "", "采集数据上传地址", ""), //
	PICTURE_STORE_DIRECTORY("PICTURE_STORE_DIRECTORY", "string", "", "图片保存地址", ""), //
	PICTURE_IS_UPLOAD("PICTURE_IS_UPLOAD", "boolean", "", "图片是否上传", ""), //
	PICTURE_UPLOAD_OVERTIME("PICTURE_IS_UPLOAD", "boolean", "", "图片上传超时时间(单位：毫秒)", ""), //
	PICTURE_UPLOAD_METHOD("PICTURE_UPLOAD_METHOD", "boolean", "", "图片上传方式", ""), //

	VIDEO_STORE_DIRECTORY("VIDEO_STORE_DIRECTORY", "string", "", "视频保存地址", ""), //
	VIDEO_IS_UPLOAD("VIDEO_IS_UPLOAD", "boolean", "", "视频是否上传", ""), //
	VIDEO_UPLOAD_OVERTIME("VIDEO_UPLOAD_OVERTIME", "number", "", "视频上传超时时间(单位：毫秒)", ""), //
	VIDEO_UPLOAD_METHOD("VIDEO_UPLOAD_METHOD", "boolean", "", "视频上传方式", ""), //
	EQUIPMENT_CONTROL_URL("VIDEO_UPLOAD_METHOD", "boolean", "", "服务设备远控地址（被调用）", ""), //
	EQUIPMENT_STATE_IS_UPLOAD("EQUIPMENT_STATE_IS_UPLOAD", "boolean", "", "设备状态报文是否上传", ""), //
	EQUIPMENT_STATE_UPLOAD_URL("EQUIPMENT_STATE_UPLOAD_URL", "boolean", "", "设备状态报文上传地址", ""), //
	UPLOAD_INTERVAL_SECOND("UPLOAD_INTERVAL_SECOND", "boolean", "", "设备状态报文上传间隔秒", ""), //
	WORKING_SAVE_DAY("WORKING_SAVE_DAY", "number", "", "过车记录保存时间（天）", ""), //
	LOG_SAVE_DAY("LOG_SAVE_DAY", "number", "", "日志保存时间（天）", ""), //
	;

	private String value;
	private String type;
	private String label;
	private Object options;
	private String customizeType;// 自定义大类
	private Object defaults;

	AttributeEnum(String value, String type, Object options, String label, String customizeType, Object defaults) {
		this.value = value;
		this.label = label;
		this.type = type;
		this.options = options;
		this.customizeType = customizeType;
		this.defaults = defaults;
	}

	AttributeEnum(String value, String type, Object options, String label, Object defaults) {
		this.value = value;
		this.label = label;
		this.type = type;
		this.options = options;
		this.customizeType = "";
		this.defaults = defaults;
	}

	public String getValue() {
		return value;
	}

	public String getLabel() {
		return label;
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

	public Object getOptions() {
		return options;
	}
}
