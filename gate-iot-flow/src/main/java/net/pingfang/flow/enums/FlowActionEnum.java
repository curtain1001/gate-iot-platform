package net.pingfang.flow.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 流程操作
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Getter
@AllArgsConstructor
public enum FlowActionEnum {

	AGREE("同意"), //
	DISAGREE("不同意"), //
	CHANGE_NODE("改签"),;

	private String note;
}
