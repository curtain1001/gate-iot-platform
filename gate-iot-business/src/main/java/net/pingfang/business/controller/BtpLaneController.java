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

import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.business.values.LaneConfig;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-22 15:47
 */
@RestController
@RequestMapping("business/lane")
public class BtpLaneController extends BaseController {

	@Resource
	private IBtpLaneService laneService;

	/**
	 * 分页列表
	 *
	 * @param lane 通道信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:lane:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpLane lane) {
		startPage();
		LambdaQueryWrapper<BtpLane> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(lane.getLaneNo()), BtpLane::getLaneNo, lane.getLaneNo());
		queryWrapper.like(StringUtils.checkValNotNull(lane.getLaneName()), BtpLane::getLaneName, lane.getLaneName());
		queryWrapper.like(StringUtils.checkValNotNull(lane.getAreaNo()), BtpLane::getAreaNo, lane.getAreaNo());
		queryWrapper.like(StringUtils.checkValNotNull(lane.getCustomsLaneNo()), BtpLane::getCustomsLaneNo,
				lane.getCustomsLaneNo());
		List<BtpLane> list = laneService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:lane:query')")
	@GetMapping(value = "/{laneId}")
	public AjaxResult getInfo(@PathVariable Long laneId) {
		return AjaxResult.success(laneService.getById(laneId));
	}

	/**
	 * 根据通道名查询
	 */
	@GetMapping(value = "/lane/{laneName}")
	public AjaxResult getLaneName(@PathVariable String laneName) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneName, laneName);
		return AjaxResult.success(laneService.getOne(wrapper));
	}

	/**
	 * 根据通道号查询
	 */
	@GetMapping(value = "/lane/{laneNo}")
	public AjaxResult getLaneNo(@PathVariable String laneNo) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneNo, laneNo);
		return AjaxResult.success(laneService.getOne(wrapper));
	}

	/**
	 * 根据海关通道号查询
	 */
	@GetMapping(value = "/lane/customs/{customsLaneNo}")
	public AjaxResult selectByCustomsLaneNo(@PathVariable String customsLaneNo) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getCustomsLaneNo, customsLaneNo);
		return AjaxResult.success(laneService.list(wrapper));
	}

	/**
	 * 根据场站号查询
	 */
	@GetMapping(value = "/lane/area/{areaNo}")
	public AjaxResult selectByAreaNo(@PathVariable String areaNo) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getAreaNo, areaNo);
		return AjaxResult.success(laneService.list(wrapper));
	}

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:lane:add')")
	@Log(title = "通道管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpLane lane) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneName, lane.getLaneName());
		if (laneService.count(wrapper) > 0) {
			return AjaxResult.error("新增通道'" + lane.getLaneName() + "'失败，通道名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneNo, lane.getLaneNo());
		if (laneService.count(wrapper) > 0) {
			return AjaxResult.error("新增通道'" + lane.getLaneNo() + "'失败，通道号已存在");
		}
		lane.setCreateBy(getUsername());
		lane.setCreateTime(new Date());
		return toAjax(laneService.save(lane));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:lane:edit')")
	@Log(title = "通道管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpLane lane) {
		LambdaQueryWrapper<BtpLane> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneName, lane.getLaneName());
		BtpLane btpLane = laneService.getOne(wrapper);
		if (btpLane != null && !btpLane.getLaneId().equals(lane.getLaneId())) {
			return AjaxResult.error("修改通道'" + lane.getLaneName() + "'失败，通道名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLane::getLaneNo, lane.getLaneNo());
		btpLane = laneService.getOne(wrapper);
		if (btpLane != null && !btpLane.getLaneId().equals(lane.getLaneId())) {
			return AjaxResult.error("修改通道'" + lane.getAreaNo() + "'失败，通道号已存在");
		}
		lane.setUpdateBy(getUsername());
		lane.setUpdateTime(new Date());
		return toAjax(laneService.updateById(lane));
	}

	/**
	 * 删除通道
	 */
	@PreAuthorize("@ss.hasPermi('business:lane:remove')")
	@Log(title = "通道管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{laneIds}")
	public AjaxResult remove(@PathVariable Long[] laneIds) {
		laneService.removeByIds(Arrays.asList(laneIds));
		return success();
	}

	/* ------------------------- 通道配置----------------------------- */
	/**
	 * 获取通道配置属性项
	 */
	@GetMapping(value = "/config/options/list")
	public AjaxResult getList() {
		return AjaxResult.success(LaneConfig.values());
	}

}
