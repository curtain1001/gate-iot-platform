package net.pingfang.flow.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.flow.domain.FlowExecuteHistory;
import net.pingfang.flow.domain.FlowProcessInstance;
import net.pingfang.flow.enums.InstanceStatus;
import net.pingfang.flow.service.IFlowExecuteHistoryService;
import net.pingfang.flow.service.IFlowProcessInstanceService;

/**
 * <p>
 * 流程实例
 * </p>
 *
 * @author 王超
 * @since 2022-08-12 15:44
 */

@RestController
@RequestMapping("/business/flow-process")
public class BtpFlowProcessController extends BaseController {
	@Resource
	IFlowProcessInstanceService processInstanceService;

	@Resource
	IFlowExecuteHistoryService executeHistoryService;

	/**
	 * 分页列表
	 *
	 * @param processInstance 流程实例信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-process:list')")
	@GetMapping("/list")
	public TableDataInfo list(FlowProcessInstance processInstance) {
		startPage();
		LambdaQueryWrapper<FlowProcessInstance> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(processInstance.getInstanceId()),
				FlowProcessInstance::getInstanceId, processInstance.getInstanceId());
		queryWrapper.like(StringUtils.checkValNotNull(processInstance.getLaneId()), FlowProcessInstance::getLaneId,
				processInstance.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(processInstance.getStatus()), FlowProcessInstance::getStatus,
				processInstance.getStatus());
		queryWrapper.like(StringUtils.checkValNotNull(processInstance.getDeployId()), FlowProcessInstance::getDeployId,
				processInstance.getDeployId());
		queryWrapper.orderByDesc(FlowProcessInstance::getStartTime);
		List<FlowProcessInstance> list = processInstanceService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-process:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult load(@PathVariable Long id) {
		return AjaxResult.success(processInstanceService.getById(id));
	}

	/**
	 * 终止流程
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-process:remove')")
	@Log(title = "流程管理(流程实例记录)", businessType = BusinessType.DELETE)
	@DeleteMapping("{instanceId}")
	public AjaxResult reset(@Validated @PathVariable(value = "instanceId") Long instanceId) {
		FlowProcessInstance instance = processInstanceService.getById(instanceId);
		if (instance == null) {
			return AjaxResult.error("该流程实例不存在");
		}
		instance = instance.toBuilder() //
				.status(InstanceStatus.TERMINATED) //
				.endTime(new Date()) //
				.build();
		return toAjax(processInstanceService.updateById(instance));
	}

	/**
	 * 修改流程
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-process:edit')")
	@Log(title = "流程管理(流程实例记录)", businessType = BusinessType.UPDATE)
	@GetMapping("record/{instanceId}")
	public AjaxResult record(@PathVariable(value = "instanceId") @Validated Long instanceId) {
		FlowProcessInstance instance = processInstanceService.getById(instanceId);
		if (instance == null) {
			return AjaxResult.error("该流程实例不存在");
		}
		LambdaQueryWrapper<FlowExecuteHistory> historyLambdaQueryWrapper = Wrappers.lambdaQuery();
		historyLambdaQueryWrapper.eq(FlowExecuteHistory::getInstanceId, instance.getInstanceId());
		historyLambdaQueryWrapper.orderByDesc(FlowExecuteHistory::getCreateTime);
		List<FlowExecuteHistory> histories = executeHistoryService.list(historyLambdaQueryWrapper);
		return AjaxResult.success(histories);
	}

}
