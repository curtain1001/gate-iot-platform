package net.pingfang.common.exception;

/**
 * @author 王超
 * @description 业务异常
 * @date 2022-07-26 0:36
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 5873411080882432103L;

	public CustomException(Throwable e) {
		super(e.getMessage(), e);
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
