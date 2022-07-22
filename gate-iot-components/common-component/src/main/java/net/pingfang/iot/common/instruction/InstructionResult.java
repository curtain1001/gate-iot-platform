package net.pingfang.iot.common.instruction;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-21 9:09
 */
public class InstructionResult<V, T> {
	/**
	 * true if success, and message should be empty
	 */
	boolean success;

	/**
	 * if success is ture, contains the error message
	 */
	String message;

	/**
	 * if success, the corresponding result
	 */
	V result;

	/**
	 * if not success, error response
	 */
	T error;

	public InstructionResult(boolean success, String message, V result, T error) {
		this.success = success;
		this.message = message;
		this.result = result;
		this.error = error;
	}

	/**
	 * 成功，返回结果
	 *
	 * @param result
	 * @param message prompt message
	 * @return
	 */
	public static <R, T> InstructionResult<R, T> success(R result, String message) {
		return new InstructionResult<>(true, message, result, null);
	}

	/**
	 * 失败，返回结果
	 *
	 * @param error
	 * @param message prompt message
	 * @return
	 */
	public static <R, T> InstructionResult<R, T> fail(T error, String message) {
		return new InstructionResult<>(false, message, null, error);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public V getResult() {
		return result;
	}

	public void setResult(V result) {
		this.result = result;
	}

	public T getError() {
		return error;
	}

	public void setError(T error) {
		this.error = error;
	}
}
