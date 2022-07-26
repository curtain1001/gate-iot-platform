package net.pingfang.flow.core;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 16:04
 */
@Data
@Builder
public class StateMachine {
	/**
	 * 持有一个状态对象
	 */
	private State state;

	/**
	 * 流程处理需要的业务数据对象
	 */
	private Object businessObj;

	public void doWork() {
		// 转调相应的状态对象真正完成功能处理
		this.state.doWork(this);
	}
}
