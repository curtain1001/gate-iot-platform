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
public enum NodeStatusEnum {

	WAIT(0, "待处理"), //
	SUCCESS(1, "成功"), //
	FAIL(2, "失败"),;

	int status;

	String name;

	public static boolean canAgree(int status) {
		return status == WAIT.status;
	}

}
