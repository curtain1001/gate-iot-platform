package net.pingfang.business.values;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-22 16:05
 */
public enum LaneConfig {
	TRIGGER_MODE("TRIGGER_MODE", "list", "[\"自动触发\",\"手动触发\"]", "触发方式"), //
	WORK_UPLOAD_URL("WORK_UPLOAD_URL", "string", "", "采集数据上传地址"), //
	PICTURE_STORE_DIRECTORY("PICTURE_STORE_DIRECTORY", "string", "", "图片保存地址"), //
	PICTURE_IS_UPLOAD("PICTURE_IS_UPLOAD", "boolean", "", "图片是否上传"), //
	PICTURE_UPLOAD_OVERTIME("PICTURE_IS_UPLOAD", "boolean", "", "图片上传超时时间(单位：毫秒)"), //
	PICTURE_UPLOAD_METHOD("PICTURE_UPLOAD_METHOD", "boolean", "", "图片上传方式"), //

	VIDEO_STORE_DIRECTORY("VIDEO_STORE_DIRECTORY", "string", "", "视频保存地址"), //
	VIDEO_IS_UPLOAD("VIDEO_IS_UPLOAD", "boolean", "", "视频是否上传"), //
	VIDEO_UPLOAD_OVERTIME("VIDEO_UPLOAD_OVERTIME", "boolean", "", "视频上传超时时间(单位：毫秒)"), //
	VIDEO_UPLOAD_METHOD("VIDEO_UPLOAD_METHOD", "boolean", "", "视频上传方式"), //
	EQUIPMENT_CONTROL_URL("VIDEO_UPLOAD_METHOD", "boolean", "", "服务设备远控地址（被调用）"), //
	EQUIPMENT_STATE_IS_UPLOAD("EQUIPMENT_STATE_IS_UPLOAD", "boolean", "", "设备状态报文是否上传"), //
	EQUIPMENT_STATE_UPLOAD_URL("EQUIPMENT_STATE_UPLOAD_URL", "boolean", "", "设备状态报文上传地址"), //
	UPLOAD_INTERVAL_SECOND("UPLOAD_INTERVAL_SECOND", "boolean", "", "设备状态报文上传间隔秒"), //
	WORKING_SAVE_DAY("WORKING_SAVE_DAY", "boolean", "", "过车记录保存时间（天）"), //
	LOG_SAVE_DAY("LOG_SAVE_DAY", "boolean", "", "日志保存时间（天）"), //

	;

	private final String key;
	private final String optionsType;
	private final Object options;

	private final String description;

	LaneConfig(String key, String optionsType, Object options, String description) {
		this.key = key;
		this.optionsType = optionsType;
		this.options = options;
		this.description = description;
	}

	public String getKey() {
		return key;
	}

	public String getOptionsType() {
		return optionsType;
	}

	public Object getOptions() {
		return options;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "LaneConfig{" + "key='" + key + '\'' + ", optionsType='" + optionsType + '\'' + ", options=" + options
				+ ", description='" + description + '\'' + '}';
	}
}
