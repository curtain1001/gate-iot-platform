//package net.pingfang.flow.service.impl;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.script.Bindings;
//import javax.script.ScriptEngine;
//import javax.script.ScriptException;
//
//import org.springframework.stereotype.Service;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//
//import net.pingfang.common.utils.StringUtils;
//import net.pingfang.flow.domain.FlowDefinitionNode;
//import net.pingfang.flow.domain.FlowVariable;
//import net.pingfang.flow.mapper.FlowDefinitionNodeMapper;
//import net.pingfang.flow.service.IFlowDefinitionNodeService;
//
///**
// * <p>
// * 流程定义节点表 服务实现类
// * </p>
// *
// * @author wangchao
// * @since 2022-07-26
// */
//@Service
//public class FlowDefinitionNodeServiceImpl extends ServiceImpl<FlowDefinitionNodeMapper, FlowDefinitionNode>
//		implements IFlowDefinitionNodeService {
//
//	@Resource
//	private FlowDefinitionNodeMapper flowDefinitionNodeMapper;
//
//	@Resource
//	private ScriptEngine scriptEngine;
//
//	@Override
//	public FlowDefinitionNode getNextNode(String definitionCode, String parentCode, List<FlowVariable> variableList) {
//		LambdaQueryWrapper<FlowDefinitionNode> queryWrapper = new LambdaQueryWrapper<>();
//		queryWrapper.eq(FlowDefinitionNode::getDefinitionCode, definitionCode);
//		if (parentCode == null) {
//			queryWrapper.isNull(FlowDefinitionNode::getParentCode);
//		} else {
//			queryWrapper.eq(FlowDefinitionNode::getParentCode, parentCode);
//		}
//		queryWrapper.orderByDesc(FlowDefinitionNode::getPriority);
//		List<FlowDefinitionNode> definitionNodeList = flowDefinitionNodeMapper.selectList(queryWrapper);
//		for (FlowDefinitionNode definitionNode : definitionNodeList) {
//			if (StringUtils.isEmpty(definitionNode.getConditionScript())) {
//				return definitionNode;
//			}
//			if (checkCondition(definitionNode.getConditionScript(), variableList)) {
//				return definitionNode;
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public FlowDefinitionNode load(String definitionCode, String nodeCode) {
//		LambdaQueryWrapper<FlowDefinitionNode> queryWrapper = new LambdaQueryWrapper<>();
//		queryWrapper.eq(FlowDefinitionNode::getDefinitionCode, definitionCode);
//		queryWrapper.eq(FlowDefinitionNode::getNodeCode, nodeCode);
//		return flowDefinitionNodeMapper.selectOne(queryWrapper);
//	}
//
//	/**
//	 * 判断脚本条件是否满足
//	 *
//	 * @param conditionScript 条件脚本
//	 * @param variableList    变量
//	 * @return 条件判断接口
//	 */
//	private boolean checkCondition(String conditionScript, List<FlowVariable> variableList) {
//		Bindings bindings = scriptEngine.createBindings();
//		for (FlowVariable variable : variableList) {
//			bindings.put(variable.getKeyy(), variable.getValuee());
//		}
//		try {
//			Object result = scriptEngine.eval(conditionScript, bindings);
//			if (result instanceof Boolean) {
//				return (Boolean) result;
//			}
//			return false;
//		} catch (ScriptException e) {
//			return false;
//		}
//	}
//}
