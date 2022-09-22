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

import net.pingfang.business.domain.BtpWorkIndex;
import net.pingfang.business.service.IBtpWorkIndexService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.utils.Column;
import net.pingfang.common.utils.FieldNameUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:41
 */
@RestController
@RequestMapping("/business/work-index")
public class BtpWorkIndexController extends BaseController {
	@Resource
	private IBtpWorkIndexService workIndexService;

	/**
	 * 分页列表
	 *
	 * @param workIndex 场站信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:workIndex:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpWorkIndex workIndex) {
		startPage();
		LambdaQueryWrapper<BtpWorkIndex> queryWrapper = Wrappers.lambdaQuery();
		// 箱区号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getAreaNo()), BtpWorkIndex::getAreaNo,
				workIndex.getAreaNo());
		// 海关通道号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getCustomNo()), BtpWorkIndex::getCustomNo,
				workIndex.getCustomNo());
		// 车架号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getFramePlate()), BtpWorkIndex::getFramePlate,
				workIndex.getFramePlate());
		// 车牌号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getLicensePlate()), BtpWorkIndex::getLicensePlate,
				workIndex.getLicensePlate());
		// 后箱号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getBackContainerNo()), BtpWorkIndex::getBackContainerNo,
				workIndex.getBackContainerNo());
		// 前箱号
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getForwardContainerNo()),
				BtpWorkIndex::getForwardContainerNo, workIndex.getForwardContainerNo());
		// 车轮毂数
		queryWrapper.like(StringUtils.checkValNotNull(workIndex.getWheelHubTotal()), BtpWorkIndex::getWheelHubTotal,
				workIndex.getWheelHubTotal());

		queryWrapper.orderByDesc(BtpWorkIndex::getCreateTime);
		List<BtpWorkIndex> list = workIndexService.list(queryWrapper);
		List<Column> columns = FieldNameUtils.getColumns(BtpWorkIndex.class);
		TableDataInfo dataInfo = getDataTable(list);
		dataInfo.setParams(columns);
		return getDataTable(list);
	}

	/**
	 * 获取所有场站信息
	 *
	 * @return 所有场站信息
	 */
	@GetMapping("/all")
	public AjaxResult getAreaLists() {
		List<BtpWorkIndex> list = workIndexService.list();
		return AjaxResult.success(list);
	}

	/**
	 * 根据参数编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('business:workIndex:query')")
	@GetMapping(value = "/{workIndexId}")
	public AjaxResult getInfo(@PathVariable Long workIndexId) {
		return AjaxResult.success(workIndexService.getById(workIndexId));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@GetMapping(value = "/workIndex/{workIndexName}")
	public AjaxResult getAreaName(@PathVariable String workIndexName) {
		LambdaQueryWrapper<BtpWorkIndex> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getAreaNo, workIndexName);
		return AjaxResult.success(workIndexService.getOne(wrapper));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@GetMapping(value = "/workIndex/{workIndexNo}")
	public AjaxResult getAreaNo(@PathVariable String workIndexNo) {
		LambdaQueryWrapper<BtpWorkIndex> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getAreaNo, workIndexNo);
		return AjaxResult.success(workIndexService.getOne(wrapper));
	}

	/**
	 * 新增场站信息
	 */
	@PreAuthorize("@ss.hasPermi('business:workIndex:add')")
	@Log(title = "场站管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpWorkIndex workIndex) {
		LambdaQueryWrapper<BtpWorkIndex> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getWorkId, workIndex.getWorkId());
		if (workIndexService.count(wrapper) > 0) {
			return AjaxResult.error("新增场站'" + workIndex.getAreaNo() + "'失败，场站名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getAreaNo, workIndex.getAreaNo());
		if (workIndexService.count(wrapper) > 0) {
			return AjaxResult.error("新增场站'" + workIndex.getAreaNo() + "'失败，场站号已存在");
		}
		workIndex.setCreateBy(getUsername());
		workIndex.setCreateTime(new Date());
		return toAjax(workIndexService.save(workIndex));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:workIndex:edit')")
	@Log(title = "场站管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpWorkIndex workIndex) {
		LambdaQueryWrapper<BtpWorkIndex> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getWorkId, workIndex.getWorkId());
		BtpWorkIndex btpArea = workIndexService.getOne(wrapper);

		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpWorkIndex::getAreaNo, workIndex.getAreaNo());
		btpArea = workIndexService.getOne(wrapper);
		workIndex.setUpdateBy(getUsername());
		workIndex.setUpdateTime(new Date());
		return toAjax(workIndexService.updateById(workIndex));
	}

	/**
	 * 删除参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:workIndex:remove')")
	@Log(title = "场站管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{workIndexIds}")
	public AjaxResult remove(@PathVariable Long[] workIndexIds) {
		workIndexService.removeByIds(Arrays.asList(workIndexIds));
		return success();
	}
}
