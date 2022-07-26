package net.pingfang.flow.core;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 16:43
 */
@Data
@Builder
public abstract class TaskNode {
	protected TaskNode successor;
	private String name;

	public TaskNode(String name) {
		this.name = name;
	}

	public abstract void process(FlowContext context);

}
