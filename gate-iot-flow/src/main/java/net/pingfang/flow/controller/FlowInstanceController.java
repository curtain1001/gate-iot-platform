package net.pingfang.flow.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.flow.domain.vo.FlowTaskVo;
import net.pingfang.flow.service.IFlowInstanceService;

/**
 * <p>
 * 工作流流程实例管理
 * <p>
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Slf4j
@RestController
@RequestMapping("/flowable/instance")
public class FlowInstanceController {

	@Autowired
	private IFlowInstanceService flowInstanceService;

	/**
	 * 根据流程定义id启动流程实例
	 *
	 * @param procDefId 流程定义id
	 * @param variables 变量集合,json对象
	 * @return
	 */
	@PostMapping("/startBy/{procDefId}")
	public AjaxResult startById(@PathVariable(value = "procDefId") String procDefId,
			@RequestBody @RequestParam(required = false) Map<String, Object> variables) {
		return flowInstanceService.startProcessInstanceById(procDefId, variables);

	}

	/**
	 * 激活或挂起流程实例
	 *
	 * @param state      1:激活,2:挂起
	 * @param instanceId 流程实例ID
	 * @return
	 */
	@PostMapping(value = "/updateState")
	public AjaxResult updateState(@RequestParam Integer state, @RequestParam String instanceId) {
		flowInstanceService.updateState(state, instanceId);
		return AjaxResult.success();
	}

	/**
	 * 结束流程实例
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/stopProcessInstance")
	public AjaxResult stopProcessInstance(@RequestBody FlowTaskVo flowTaskVo) {
		flowInstanceService.stopProcessInstance(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 删除流程实例
	 *
	 * @param instanceId   流程实例ID
	 * @param deleteReason 删除原因
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResult delete(@RequestParam String instanceId, String deleteReason) {
		flowInstanceService.delete(instanceId, deleteReason);
		return AjaxResult.success();
	}
}
