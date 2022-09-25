package net.pingfang.business.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpWorkIndex;
import net.pingfang.business.service.IBtpWorkIndexService;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
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

	@GetMapping("/columns")
	public AjaxResult getColumns() {
		return AjaxResult.success(FieldNameUtils.getColumns(BtpWorkIndex.class));
	}

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
}
