package net.pingfang.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 流程节点状态
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Getter
@AllArgsConstructor
public enum ProcessStatus {

	WAIT("wait", "待处理"), //
	SUCCESS("success", "成功"), //
	FAIL("fail", "失败"),;

	private String status;

	private String name;

	/**
	 * 可以处理
	 *
	 * @param status 状态
	 * @return 是否可以处理
	 */
	public static boolean dealWith(String status) {
		return ProcessStatus.valueOf(status) == WAIT;
	}

}
