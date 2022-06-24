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

import net.pingfang.business.component.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.business.domain.BtpLaneConfig;
import net.pingfang.business.service.IBtpLaneConfigService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-23 14:48
 */
@RestController
@RequestMapping("business/lane/config")
public class BtpLaneConfigController extends BaseController {
	@Resource
	private IBtpLaneConfigService laneConfigService;

	/* ------------------------- 通道配置----------------------------- */
	/**
	 * 获取通道配置属性项
	 */
	@GetMapping(value = "/options")
	public AjaxResult getList() {
		return AjaxResult.success(CustomizedSettingRepository.getValues());
	}

	/**
	 * 分页列表
	 *
	 * @param laneConfig 通道信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:laneconfig:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpLaneConfig laneConfig) {
		startPage();
		LambdaQueryWrapper<BtpLaneConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(StringUtils.checkValNotNull(laneConfig.getLaneId()), BtpLaneConfig::getLaneId,
				laneConfig.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(laneConfig.getLaneConfigKey()), BtpLaneConfig::getLaneConfigKey,
				laneConfig.getLaneConfigKey());
		List<BtpLaneConfig> list = laneConfigService.list(queryWrapper);
		return getDataTable(list);
	}

	@PostMapping("")
	@PreAuthorize("@ss.hasPermi('business:laneconfig:add')")
	@Log(title = "通道管理(配置信息)", businessType = BusinessType.UPDATE)
	public AjaxResult add(BtpLaneConfig laneConfig) {
		LambdaQueryWrapper<BtpLaneConfig> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLaneConfig::getLaneConfigKey, laneConfig.getLaneConfigKey());
		wrapper.eq(BtpLaneConfig::getLaneId, laneConfig.getLaneId());
		if (laneConfigService.count(wrapper) > 0) {
			return AjaxResult.error("新增通道配置'" + laneConfig.getLaneConfigKey() + "'失败，该通道配置已存在");
		}
		laneConfig.setCreateBy(getUsername());
		laneConfig.setCreateTime(new Date());
		return toAjax(laneConfigService.save(laneConfig));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:laneconfig:edit')")
	@Log(title = "通道管理(配置信息)", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpLaneConfig laneConfig) {
		LambdaQueryWrapper<BtpLaneConfig> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpLaneConfig::getLaneConfigKey, laneConfig.getLaneConfigKey());
		wrapper.eq(BtpLaneConfig::getLaneConfigId, laneConfig.getLaneConfigId());
		BtpLaneConfig btpLaneConfig = laneConfigService.getOne(wrapper);
		if (btpLaneConfig != null && !btpLaneConfig.getLaneConfigId().equals(btpLaneConfig.getLaneConfigId())) {
			return AjaxResult.error("修改通道配置'" + laneConfig.getLaneConfigKey() + "'失败，该通道配置已存在");
		}
		laneConfig.setUpdateBy(getUsername());
		laneConfig.setUpdateTime(new Date());
		return toAjax(laneConfigService.updateById(laneConfig));
	}

	/**
	 * 删除通道
	 */
	@PreAuthorize("@ss.hasPermi('business:laneconfig:remove')")
	@Log(title = "通道管理(配置信息)", businessType = BusinessType.DELETE)
	@DeleteMapping("/{laneConfigIds}")
	public AjaxResult remove(@PathVariable Long[] laneConfigIds) {
		laneConfigService.removeByIds(Arrays.asList(laneConfigIds));
		return success();
	}
}
