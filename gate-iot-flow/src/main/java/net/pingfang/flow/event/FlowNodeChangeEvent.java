package net.pingfang.flow.event;

import lombok.Builder;
import lombok.Data;
import net.pingfang.flow.domain.FlowNode;

/**
 * <p>
 * 流程节点状态变更事件
 * </p>
 *
 * @author 王超
 * @since 2022-08-15 17:46
 */
@Data
@Builder
public class FlowNodeChangeEvent {
	/**
	 * 流程id
	 */
	private Long instanceId;
	/**
	 * 车道id
	 */
	private final Long laneId;
	/**
	 * type start：开始节点 finish：完成节点
	 */
	private final String type;
	/**
	 * 节点数据
	 */
	private final FlowNode flowNode;

}
