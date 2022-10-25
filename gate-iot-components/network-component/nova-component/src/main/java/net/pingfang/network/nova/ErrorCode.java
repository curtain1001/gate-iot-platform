package net.pingfang.network.nova;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 16:01
 */
@AllArgsConstructor
@Getter
public class ErrorCode {
	final static Map<Integer, String> errormap = Maps.newHashMap();
	static {
		errormap.put(-1, "找不到IP对应主机");
		errormap.put(-2, "IO错误");
		errormap.put(-3, "文件名发送未收到回复");
		errormap.put(-4, "文件名接收到错误");
		errormap.put(-5, "找不到IP对应主机");
		errormap.put(-6, "找不到IP对应主机");
		errormap.put(-7, "找不到IP对应主机");
		errormap.put(-8, "找不到IP对应主机");
		errormap.put(-9, "找不到IP对应主机");
		errormap.put(-10, "找不到IP对应主机");
		errormap.put(-11, "找不到IP对应主机");
		errormap.put(-12, "找不到IP对应主机");
		errormap.put(-13, "找不到IP对应主机");
		errormap.put(-14, "找不到IP对应主机");
		errormap.put(-15, "找不到IP对应主机");
	}

	public String getMsg(int code) {
		return errormap.get(code);
	}

}
