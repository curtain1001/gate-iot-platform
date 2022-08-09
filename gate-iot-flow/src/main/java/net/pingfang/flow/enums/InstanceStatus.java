package net.pingfang.flow.enums;

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
public enum InstanceStatus {

	IN_PROGRESS("进行中"), //
	FINISHED("已完成"), //
	TERMINATED("已终止"),;

	private final String name;

	InstanceStatus(String name) {
		this.name = name;
	}

	public boolean inProgress() {
		return this == IN_PROGRESS;
	}

}
