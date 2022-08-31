package net.pingfang.device.novaled.utils;

import java.util.Arrays;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-19 13:48
 */
public enum ResultCode {

	SUCCESS(1, "成功"), //
	UNKNOWN_HOST(-1, "未知主机"), //
	IO_ERROR(-2, "IO错误"), //
	SEND_FILE_NAME_NOT_RECV_ERROR(-3, "发送文件名未接收错误"), //
	SEND_FILE_NAME_RECV_ERROR(-4, "发送文件名接收错误"), //
	SEND_FILE_BLOCK_NOT_RECV_ERROR(-5, ""), //
	SEND_FILE_BLOCK_RECV_ERROR(-6, ""), //
	SEND_FILE_BLOCK_NOT_RECV_END_ERROR(-7, ""), //
	SEND_FILE_BLOCK_RECV_END_ERROR(-8, ""), //
	PLAY_LIST_NOT_RECV_ERROR(-9, ""), //
	PLAY_LIST_RECV_ERROR(-10, ""), //
	SEND_NOT_RECV_ERROR(-11, ""), //
	SEND_RECV_ERROR(-12, ""), //
	FILE_NOT_EXIST_ERROR(-13, ""), //
	SOCKET_ERROR(-14, ""), //
	PARAM_ERROR(-15, ""), //
	UNKNOWN_ERROR(-101, "未知错误");

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
		return Arrays.stream(ResultCode.values()).filter(x -> x.key == code).findFirst().orElse(UNKNOWN_ERROR).value;
	}
}
