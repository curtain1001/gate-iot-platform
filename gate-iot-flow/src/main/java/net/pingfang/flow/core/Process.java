package net.pingfang.flow.core;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-26 15:57
 */
@Data
@Builder
public class Process {

	final String processId;

	final Long laneId;

	TaskNode<?> taskNode;

	List<TaskNode<?>> startNode;

	List<TaskNode<?>> currentNode;

	List<TaskNode<?>> endNode;

}
