package net.pingfang.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 流程实例状态
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Getter
@AllArgsConstructor
public enum InstanceStatusEnum {

	IN_PROGRESS(0, "进行中"), //
	FINISHED(1, "已完成"), //
	TERMINATED(2, "已终止"),;

	int status;

	String name;

	public static boolean inProgress(int status) {
		return status == IN_PROGRESS.status;
	}

}
