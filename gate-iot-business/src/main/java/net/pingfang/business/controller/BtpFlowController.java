package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpFlow;
import net.pingfang.business.service.impl.BtpFlowServiceImpl;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 17:26
 */
@RestController
@RequestMapping("business/flow")
public class BtpFlowController extends BaseController {

	@Resource
	private BtpFlowServiceImpl flowService;

	/**
	 * 分页列表
	 *
	 * @param flow 流程信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpFlow flow) {
		startPage();
		LambdaQueryWrapper<BtpFlow> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(flow.getFlowId()), BtpFlow::getFlowId, flow.getFlowId());
		queryWrapper.like(StringUtils.checkValNotNull(flow.getFlowName()), BtpFlow::getFlowName, flow.getFlowName());
		queryWrapper.like(StringUtils.checkValNotNull(flow.getLaneId()), BtpFlow::getLaneId, flow.getLaneId());
		List<BtpFlow> list = flowService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 分页列表
	 *
	 * @return 所有设备
	 */
	@GetMapping("/all/{laneId}")
	public AjaxResult getFlows(@PathVariable("laneId") Long laneId) {
		LambdaQueryWrapper<BtpFlow> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpFlow::getLaneId, laneId);
		List<BtpFlow> list = flowService.list(queryWrapper);
		return AjaxResult.success(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:query')")
	@GetMapping(value = "/{flowId}")
	public AjaxResult load(@PathVariable Long flowId) {
		return AjaxResult.success(flowService.getById(flowId));
	}

	/**
	 * 根据流程名查询
	 */
	@GetMapping(value = "flow-name/{flowName}")
	public AjaxResult getLaneName(@PathVariable String flowName) {
		LambdaQueryWrapper<BtpFlow> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpFlow::getFlowName, flowName);
		return AjaxResult.success(flowService.getOne(wrapper));
	}

	/**
	 * 新增流程信息
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:add')")
	@Log(title = "流程管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpFlow flow) {
		LambdaQueryWrapper<BtpFlow> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpFlow::getFlowName, flow.getFlowName());
		wrapper.eq(BtpFlow::getLaneId, flow.getLaneId());
		if (flowService.count(wrapper) > 0) {
			return AjaxResult.error("新增流程'" + flow.getFlowName() + "'失败，该流程名称已存在");
		}
		flow.setCreateBy(getUsername());
		flow.setCreateTime(new Date());
		return toAjax(flowService.save(flow));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:edit')")
	@Log(title = "流程管理", businessType = BusinessType.UPDATE)
	@PutMapping()
	public AjaxResult edit(@Validated @RequestBody BtpFlow flow) {
		LambdaQueryWrapper<BtpFlow> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpFlow::getFlowName, flow.getFlowName());
		wrapper.eq(BtpFlow::getLaneId, flow.getLaneId());
		BtpFlow oldFlow = flowService.getOne(wrapper);
		if (oldFlow != null && !oldFlow.getFlowId().equals(flow.getFlowId())) {
			return AjaxResult.error("修改流程'" + flow.getFlowName() + "'失败，流程名已存在");
		}
		flow.setUpdateBy(getUsername());
		flow.setUpdateTime(new Date());
		return toAjax(flowService.updateById(flow));
	}

	/**
	 * 部署流程
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:edit')")
	@Log(title = "流程管理", businessType = BusinessType.GRANT)
	@PutMapping("/deploy/{laneId}/{flowId}/{version}")
	public AjaxResult deploy(@PathVariable("laneId") Long laneId, @PathVariable("flowId") @Validated Long flowId,
			@PathVariable("version") Integer version) {
		// 流程部署
		return toAjax(flowService.deploy(laneId, flowId, version));
	}

	/**
	 * 删除流程
	 */
	@PreAuthorize("@ss.hasPermi('business:flow:remove')")
	@Log(title = "流程管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		flowService.removeByIds(Arrays.asList(ids));
		return AjaxResult.success();
	}

}
