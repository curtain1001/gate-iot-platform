package net.pingfang.flow.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.flow.domain.FlowDefinitionNode;
import net.pingfang.flow.domain.FlowVariable;

/**
 * <p>
 * 流程定义节点表 服务类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
public interface IFlowDefinitionNodeService extends IService<FlowDefinitionNode> {
	/**
	 * 获取下一个流程定义节点
	 *
	 * @param definitionCode 流程定义编号
	 * @param parentCode     父节点
	 * @param variables      变量
	 * @return 下一个流程节点
	 */
	FlowDefinitionNode getNextNode(String definitionCode, String parentCode, List<FlowVariable> variables);

	/**
	 * 获取流程节点
	 *
	 * @author lijile
	 * @date 2022/1/18 18:40
	 * @param definitionCode
	 * @param nodeCode
	 * @return
	 */
	FlowDefinitionNode load(String definitionCode, String nodeCode);
}
