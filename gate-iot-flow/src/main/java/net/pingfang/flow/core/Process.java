package net.pingfang.flow.core;

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

	private String processId;
	/**
	 * 定义代码
	 */
	private String definitionCode;

	/**
	 * 流程定义名称
	 */
	private String definitionName;
	/**
	 * 所有流程节点
	 */
//	List<FlowDefinitionNode> taskNodeList;
	/**
	 * 版本号
	 */
	private String version;

}
