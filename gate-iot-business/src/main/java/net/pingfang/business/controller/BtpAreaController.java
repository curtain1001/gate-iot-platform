package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

import net.pingfang.business.domain.BtpArea;
import net.pingfang.business.service.IBtpAreaService;
import net.pingfang.business.values.LabelObject;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;

/**
 * @author 王超
 * @description 场站信息
 * @date 2022-06-22 11:36
 */
@RestController
@RequestMapping("/business/area")
public class BtpAreaController extends BaseController {
	@Resource
	private IBtpAreaService areaService;

	/**
	 * 分页列表
	 *
	 * @param area 场站信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:area:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpArea area) {
		startPage();
		LambdaQueryWrapper<BtpArea> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(area.getAreaName()), BtpArea::getAreaName, area.getAreaName());
		queryWrapper.like(StringUtils.checkValNotNull(area.getAreaNo()), BtpArea::getAreaNo, area.getAreaNo());
		List<BtpArea> list = areaService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 获取所有场站信息
	 *
	 * @return 所有场站信息
	 */
	@GetMapping("/all")
	public AjaxResult getAreaLists() {
		List<BtpArea> list = areaService.list();
		return AjaxResult.success(list);
	}

	/**
	 * 根据参数编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('business:area:query')")
	@GetMapping(value = "/{areaId}")
	public AjaxResult getInfo(@PathVariable Long areaId) {
		return AjaxResult.success(areaService.getById(areaId));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@GetMapping(value = "/area/{areaName}")
	public AjaxResult getAreaName(@PathVariable String areaName) {
		LambdaQueryWrapper<BtpArea> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaName, areaName);
		return AjaxResult.success(areaService.getOne(wrapper));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@GetMapping(value = "/area/{areaNo}")
	public AjaxResult getAreaNo(@PathVariable String areaNo) {
		LambdaQueryWrapper<BtpArea> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaNo, areaNo);
		return AjaxResult.success(areaService.getOne(wrapper));
	}

	/**
	 * 新增场站信息
	 */
	@PreAuthorize("@ss.hasPermi('business:area:add')")
	@Log(title = "场站管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpArea area) {
		LambdaQueryWrapper<BtpArea> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaName, area.getAreaName());
		if (areaService.count(wrapper) > 0) {
			return AjaxResult.error("新增场站'" + area.getAreaNo() + "'失败，场站名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaNo, area.getAreaNo());
		if (areaService.count(wrapper) > 0) {
			return AjaxResult.error("新增场站'" + area.getAreaNo() + "'失败，场站号已存在");
		}
		area.setCreateBy(getUsername());
		area.setCreateTime(new Date());
		return toAjax(areaService.save(area));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:area:edit')")
	@Log(title = "场站管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpArea area) {
		LambdaQueryWrapper<BtpArea> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaName, area.getAreaName());
		BtpArea btpArea = areaService.getOne(wrapper);
		if (btpArea != null && !btpArea.getAreaId().equals(area.getAreaId())) {
			return AjaxResult.error("修改场站'" + area.getAreaName() + "'失败，场站名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpArea::getAreaNo, area.getAreaNo());
		btpArea = areaService.getOne(wrapper);
		if (btpArea != null && !btpArea.getAreaId().equals(area.getAreaId())) {
			return AjaxResult.error("修改场站'" + area.getAreaNo() + "'失败，场站号已存在");
		}
		area.setUpdateBy(getUsername());
		area.setUpdateTime(new Date());
		return toAjax(areaService.updateById(area));
	}

	/**
	 * 删除参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:area:remove')")
	@Log(title = "场站管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{areaIds}")
	public AjaxResult remove(@PathVariable Long[] areaIds) {
		areaService.removeByIds(Arrays.asList(areaIds));
		return success();
	}

	@GetMapping("list/all")
	public AjaxResult getList() {
		List<BtpArea> btpAreas = areaService.getAll();
		List<LabelObject> labelObjects = btpAreas.stream().map(BtpArea::toLabelObject).collect(Collectors.toList());
		return AjaxResult.success(labelObjects);
	}

}
