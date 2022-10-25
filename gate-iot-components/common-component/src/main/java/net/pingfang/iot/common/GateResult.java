package net.pingfang.iot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 执行动作的响应结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GateResult<T> {

	protected boolean success;
	protected String errorMsg;
	protected T data;

	/**
	 * 返回成功的实例.
	 */
	public static <T> GateResult<T> ok() {
		return new GateResult<>(true, null, null);
	}

	/**
	 * 创建成功的实例.
	 */
	public static <T> GateResult<T> ok(T data) {
		return new GateResult<>(true, null, data);
	}

	/**
	 * 创建失败的实例.
	 */
	public static <T> GateResult<T> fail(String msg) {
		return new GateResult<>(false, msg, null);
	}

	/**
	 * 创建失败的实例.
	 */
	public static <T> GateResult<T> fail(String msg, T data) {
		return new GateResult<>(false, msg, data);
	}

	/**
	 * 设置错误码，错误消息，成功状态.
	 */
	public void set(GateResult data) {
		this.success = data.success;
		this.errorMsg = data.errorMsg;
	}

	/**
	 * 获取错误信息，格式: $code,$msg
	 */
	public String getError() {
		if (success) {
			return "";
		}
		return errorMsg;
	}

}
