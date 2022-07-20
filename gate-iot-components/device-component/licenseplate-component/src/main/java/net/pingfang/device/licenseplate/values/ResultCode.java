package net.pingfang.device.licenseplate.values;

import java.util.Arrays;

/**
 * @author 王超
 * @description TODO
 * @date 2022-04-22 12:15
 */
public enum ResultCode {
	DC_UNDEFINED_ERROR(-1, "未定义错误"), //
	DC_NO_ERROR(0, "正常"), //
	DC_HANDLE_INVALID(1, "无效句柄"), //
	DC_CONN_FAIL(2, "连接失败"), //
	DC_OBJ_BUSY(3, "对象忙"), //
	DC_OBJ_UNEXIST(4, "对象不存在"), //
	DC_CMD_INVALID(5, "命令无效"), //
	DC_PARA_INVALID(6, "参数无效"), //
	DC_REQ_TIMEOUT(7, "请求超时"), //
	DC_MEMORY_LACK(8, "内存申请失败"), //
	DC_SEND_FAIL(9, "数据发送失败"), //
	DC_RECV_FAIL(10, "数据接收失败"), //
	DC_OPT_FAIL(11, "操作失败"), //
	DC_NOT_CONN(12, "没有触发连接"), //
	DC_BEYOND_MAX_CLIENT(13, "超出相机最大连接数量"), //
	DC_CONNECT_AUTH(18, "连接鉴权"), //
	DC_USER_NOT_EXIST(19, "用户不存在"), //
	DC_PASSWD_ERROR(20, "密码错误"), //
	DC_TF_NOT_EXIST(21, "TF卡不存在"), //
	DC_TF_IO_ERROR(22, "TF卡读写错误"), //
	DC_ENCPYPTION_ERR(23, "二次加密数据校验失败"), //
	DC_CONN_PORT_NEGO_FAIL(1000, "发送端口协商失败"), //
	DC_CONN_RXQUE_CREATE_FAIL(1001, "创建接收队列失败"), //
	DC_CONN_TXQUE_CREATE_FAIL(1002, "创建发送队列失败"), //
	DC_CONN_REQUE_CREATE_FAIL(1003, "创建消息上报队列失败"), //
	DC_CONN_30000_CONTROL_TSK_CREATE_FAIL(1004, "创建30000控制线程失败"), //
	DC_CONN_40000_CONTROL_TSK_CREATE_FAIL(1005, "创建图片上报线程失败"), //
	DC_CONN_SERVER_ERROR(1006, "服务器内部错误"), //
	DC_CONN_RESPONSE_CODE_AUTHORITY_LIMIT(1007, "权限受限"), //
	DC_WHITE_LIST_FILE_OPEN_FAIL(1100, "白名单文件打开失败"), //
	DC_WHITE_LIST_FILE_EMPTY(1101, "白名单文件为空"), //
	DC_WHITE_LIST_FILE_MAPPING_FAIL(1102, "白名单文件映射失败"), //
	DC_WHITE_LIST_FILE_MAPPING_OPEN_FAIL(1103, "白名单文件打开映射失败"), //
	DC_WHITE_LIST_MEMORY_LACK_FAIL(1104, "白名单内存申请失败"), //
	DC_WHITE_LIST_FILE_SIZE_MORE(1105, "白名单文件大于1M"), //
	DC_WHITE_LIST_FORMAT_ERROR(1106, "白名单格式不正确"), //
	DC_WHITE_LIST_CREATE_NEW_FILE_FAIL(1107, "创建新的白名单失败"), //
	DC_WHITE_LIST_WRITE_NEW_FILE_FAIL(1108, "写白名单文件失败"), //
	DC_WHITE_LIST_WRITE_NEW_FILE_LENGTH_ERROR(1109, "写白名单长度不对"), //
	DC_WHITE_LIST_MEMORY_LACK_FAIL_OLD(1110, "白名单内存申请失败"), //
	DC_WHITE_LIST_CREATE_THREAD_FAIL(1111, "白名单创建线程失败"), //
	DC_WHITE_LIST_PLATE_EMPTY(1112, "白名单车牌号为空"), //
	DC_WHITE_LIST_PLATE_LENGTH_ERROR(1113, "白名单车牌长度错误"), //
	DC_WHITE_LIST_PLATE_SPECIAL_CHAR(1114, "白名单车牌包含特殊字符"), //
	DC_WHITE_LIST_TIME_YEAR_ERROR(1115, "白名单时间年份不对"), //
	DC_WHITE_LIST_TIME_MONTH_ERROR(1117, "白名单时间月份不对"), //
	DC_WHITE_LIST_TIME_BIG_MONTH_DAY_ERROR(1118, "白名单时间大月日期不对"), //
	DC_WHITE_LIST_TIME_SMALL_MONTH_DAY_ERROR(1119, "白名单时间小月日期不对"), //
	DC_WHITE_LIST_TIME_LEAP_YEAR_DAY_ERROR(1120, "白名单时间闰年二月日期不对"), //
	DC_WHITE_LIST_TIME_NO_LEAP_YEAR_DAY_ERROR(1121, "白名单时间非闰年二月日期不对"), //
	DC_WHITE_LIST_TIME_HOUR_ERROR(1122, "白名单时间小时不对"), //
	DC_WHITE_LIST_TIME_MINUTE_ERROR(1123, "白名单时间分钟不对"), //
	DC_WHITE_LIST_TIME_SECOND_ERROR(1124, "白名单时间秒数不对"), //
	DC_WHITE_LIST_TIME_START_THAN_END(1125, "白名单时间开始时间大于结束时间"), //
	DC_WHITE_LIST_EXPORT_CSV_READ_FAIL(1126, "白名单导出CSV格式读失败"), //
	DC_WHITE_LIST_EXPORT_CSV_WRITE_FAIL(1127, "白名单导出CSV格式写失败"), //
	DC_UPLOAD_DATA_SERVER_ALLOC_FAIL(1200, "上传服务创建失败"), //
	DC_UPLOAD_UNABLE_TO_OPEN_FILE(1201, "上传文件不能访问"), //
	DC_UPLOAD_SOCKET_CREATE_FAIL(1202, "上传服务创建socket失败"), //
	DC_UPLOAD_CONNECT_CAMERA_FAIL(1203, "上传服务连接相机失败"), //
	DC_UPLOAD_READ_FILE_FAIL(1204, "上传文件读取失败"), //
	DC_UPLOAD_SEND_FILE_NO_ALL(1205, "上传文件发送不完整"), //
	DC_UPLOAD_DATA_SERVER_ALLOC_FAIL_OLD(1220, "上传服务创建失败"), //
	DC_UPLOAD_DATA_CREATE_THREAD_FAIL_OLD(1221, "上传服务创建线程失败"), //
	DC_DOWNLOAD_DATA_SERVER_ALLOC_FAIL(1250, "下载服务创建失败"), //
	DC_DOWNLOAD_SOCKET_CREATE_FAIL(1251, "下载服务创建socket失败"), //
	DC_DOWNLOAD_CONNECT_CAMERA_FAIL(1252, "下载服务连接相机失败"), //
	DC_DOWNLOAD_CREATE_FILE_FAIL(1253, "下载服务创建文件失败"), //
	DC_DOWNLOAD_RECV_DATA_FAIL(1254, "下载服务接收数据失败"), //
	DC_GET_PORT_SOCKET_ERROR(1300, "获取本地端口socket错误"), //
	DC_GET_PORT_GET_LOCAL_IP_FAIL(1301, "获取本地端口获取本地IP失败"), //
	DC_GET_PORT_FAIL(1302, "获取本地端口失败"), //
	DC_UPDATE_FILE_TYPE_ERROR(1400, "升级文件类型不对");//

	public final int key;
	public final String value;

	ResultCode(int key, String value) {
		this.key = key;
		this.value = value;
	}

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String getMsg(int code) {
		return Arrays.stream(ResultCode.values()).filter(x -> x.key == code).findFirst()
				.orElse(DC_UNDEFINED_ERROR).value;
	}

}
