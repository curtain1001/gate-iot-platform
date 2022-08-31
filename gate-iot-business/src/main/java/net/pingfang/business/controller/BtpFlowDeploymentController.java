package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
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
import net.pingfang.flow.domain.FlowDeployment;
import net.pingfang.flow.service.IFlowDeploymentService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-08 16:36
 */
@RequestMapping("business/flow-deployment")
@RestController
public class BtpFlowDeploymentController extends BaseController {

	@Resource
	public IFlowDeploymentService deploymentService;

	@GetMapping("all")
	public AjaxResult getDeployAll() {
		List<FlowDeployment> flowDeploymentList = deploymentService.list();
		return AjaxResult.success(flowDeploymentList);
	}

	@GetMapping("lane/{laneId}")
	public AjaxResult getDeployByLaneId(@PathVariable("laneId") Long laneId) {
		LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(FlowDeployment::getLaneId, laneId);
		List<FlowDeployment> flowDeploymentList = deploymentService.list(queryWrapper);
		return AjaxResult.success(flowDeploymentList);
	}

	/**
	 * 分页列表
	 *
	 * @param deployment 流程信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-deployment:list')")
	@GetMapping("/list")
	public TableDataInfo list(FlowDeployment deployment) {
		startPage();
		LambdaQueryWrapper<FlowDeployment> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(deployment.getLaneId()), FlowDeployment::getLaneId,
				deployment.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(deployment.getFlowId()), FlowDeployment::getFlowId,
				deployment.getFlowId());
		queryWrapper.orderByDesc(FlowDeployment::getCreateTime);
		List<FlowDeployment> list = deploymentService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-deployment:query')")
	@GetMapping(value = "/{deployId}")
	public AjaxResult load(@PathVariable Long deployId) {
		return AjaxResult.success(deploymentService.getById(deployId));
	}

	/**
	 * 停止部署
	 *
	 * @param laneIds laneId 车道id
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-deployment:remove')")
	@Log(title = "流程管理(部署)", businessType = BusinessType.DELETE)
	@DeleteMapping("laneId/{laneIds}")
	public AjaxResult removeByLaneId(@PathVariable Long[] laneIds) {
		Arrays.stream(laneIds).forEach(id -> {
			deploymentService.removeByLaneId(id);
		});
		return AjaxResult.success();
	}

	/**
	 * 停止部署
	 *
	 * @param ids 主键id
	 */
	@PreAuthorize("@ss.hasPermi('business:flow-deployment:remove')")
	@Log(title = "流程管理(部署)", businessType = BusinessType.DELETE)
	@DeleteMapping("{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		deploymentService.removeByIds(Arrays.asList(ids));
		return AjaxResult.success();
	}

}
