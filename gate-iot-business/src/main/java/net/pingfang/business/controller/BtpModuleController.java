package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpModule;
import net.pingfang.business.service.IBtpModuleService;
import net.pingfang.business.values.KeyValuePair;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.iot.application.service.ServiceProduct;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-05 10:22
 */
@RestController
@RequestMapping("business/module")
public class BtpModuleController extends BaseController {

	private IBtpModuleService moduleService;

	/**
	 * 分页列表
	 *
	 * @param module 模块信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:module:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpModule module) {
		startPage();
		LambdaQueryWrapper<BtpModule> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(module.getModuleName()), BtpModule::getModuleName,
				module.getModuleName());
		queryWrapper.like(StringUtils.checkValNotNull(module.getModuleCode()), BtpModule::getModuleCode,
				module.getModuleName());
		queryWrapper.in(StringUtils.checkValNotNull(module.getDeviceId()), BtpModule::getDevice, module.getDeviceId());
		return getDataTable(moduleService.list(queryWrapper));
	}

	@GetMapping("/support")
	public AjaxResult getModuleSupport() {
		return AjaxResult.success(Arrays.stream(ServiceProduct.values()) //
				.map(x -> KeyValuePair.builder() //
						.key(x.name()) //
						.value(x.getName()) //
						.build()) //
				.collect(Collectors.toList()));
	}

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:module:add')")
	@Log(title = "服务模块管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpModule module) {
		LambdaQueryWrapper<BtpModule> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpModule::getLaneId, module.getLaneId());
		wrapper.eq(BtpModule::getModuleName, module.getModuleName());
		if (moduleService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务模块'" + module.getModuleName() + "'失败，服务模块名已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpModule::getLaneId, module.getLaneId());
		wrapper.eq(BtpModule::getModuleCode, module.getModuleCode());
		if (moduleService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务模块'" + module.getModuleCode() + "'失败，服务模块号已存在");
		}

		module.setCreateBy(getUsername());
		module.setCreateTime(new Date());

		return toAjax(moduleService.save(module));
	}

}
