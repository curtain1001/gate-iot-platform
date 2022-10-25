package net.pingfang.business.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpServiceDevice;
import net.pingfang.business.domain.BtpSupportService;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpServiceDeviceService;
import net.pingfang.business.service.IBtpSupportServiceService;
import net.pingfang.business.values.KeyValuePair;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.services.ServiceProduct;
import net.pingfang.services.ServiceProductSupports;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 10:51
 */
@RestController
@RequestMapping("business/support-service")
public class BtpSupportServiceController extends BaseController {
	@Resource
	public IBtpSupportServiceService supportService;
	@Resource
	public IBtpDeviceService deviceService;
	@Resource
	public IBtpServiceDeviceService serviceDeviceService;
	@Resource
	public EventBusCenter eventBusCenter;

	public ServiceProduct serviceProduct = ServiceProduct.IC_OCR;

	@GetMapping("/product/list")
	public AjaxResult getServiceSupports() {
		return AjaxResult.success(ServiceProductSupports.getServiceSupports());
	}

	/**
	 * 分页列表
	 *
	 * @param btpSupportService 支撑服务信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpSupportService btpSupportService) {
		startPage();
		LambdaQueryWrapper<BtpSupportService> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(StringUtils.checkValNotNull(btpSupportService.getLaneId()), BtpSupportService::getLaneId,
				btpSupportService.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(btpSupportService.getServiceCode()),
				BtpSupportService::getServiceCode, btpSupportService.getServiceCode());
		queryWrapper.like(StringUtils.checkValNotNull(btpSupportService.getServiceName()),
				BtpSupportService::getServiceName, btpSupportService.getServiceName());
		queryWrapper.orderByDesc(BtpSupportService::getCreateTime);
		List<BtpSupportService> list = supportService.list(queryWrapper);
		TableDataInfo tableDataInfo = getDataTable(list);
		tableDataInfo.setRows(tableDataInfo.getRows().stream().map(x -> {
			BtpSupportService support = ((BtpSupportService) x);
			LambdaQueryWrapper<BtpServiceDevice> lambdaQueryWrapper = Wrappers.lambdaQuery();
			lambdaQueryWrapper.in(BtpServiceDevice::getServiceId, support.getId());
			List<BtpServiceDevice> serviceDevices = serviceDeviceService.list(lambdaQueryWrapper);
			if (CollectionUtils.isNotEmpty(serviceDevices)) {
				List<Long> deviceIds = serviceDevices.stream().map(BtpServiceDevice::getDeviceId)
						.collect(Collectors.toList());
				List<BtpDevice> devices = deviceService.listByIds(deviceIds);
				return support.toBuilder() //
						.devices(devices.stream().map(device -> KeyValuePair.builder() //
								.key(device.getDeviceId()) //
								.value(device.getDeviceName())//
								.build()).collect(Collectors.toList())) //
						.build(); //
			}
			return support;
		}).collect(Collectors.toList()));
		return tableDataInfo;
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult load(@PathVariable Long id) {
		return AjaxResult.success(supportService.getById(id));
	}

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:add')")
	@Log(title = "服务管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpSupportService btpSupportService) {
		LambdaQueryWrapper<BtpSupportService> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpSupportService::getLaneId, btpSupportService.getLaneId());
		wrapper.eq(BtpSupportService::getServiceCode, btpSupportService.getServiceCode());
		if (supportService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务配置'" + btpSupportService.getServiceName() + "'失败，该服务配置已存在");
		}
		btpSupportService.setCreateBy(getUsername());
		btpSupportService.setCreateTime(new Date());
//		postEvent(btpSupportService);
		return toAjax(supportService.save(btpSupportService));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:edit')")
	@Log(title = "服务管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpSupportService btpSupportService) {
		LambdaQueryWrapper<BtpSupportService> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpSupportService::getLaneId, btpSupportService.getLaneId());
		wrapper.eq(BtpSupportService::getServiceCode, btpSupportService.getServiceCode());
		BtpSupportService btpSupportServer = supportService.getOne(wrapper);
		if (btpSupportServer != null && !btpSupportServer.getId().equals(btpSupportService.getId())) {
			return AjaxResult.error("修改配置'" + btpSupportService.getServiceCode() + "'失败，服务配置已存在");
		}
		btpSupportService.setUpdateBy(getUsername());
		btpSupportService.setUpdateTime(new Date());
//		postEvent(btpSupportService);
		return toAjax(supportService.updateById(btpSupportService));
	}

	/**
	 * 开启设备
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:open')")
	@Log(title = "服务管理", businessType = BusinessType.UPDATE)
	@PutMapping("{id}/on}")
	public AjaxResult openService(@Validated @PathVariable Long id, @Validated @PathVariable String status) {
		BtpSupportService btpSupportService = supportService.getById(id);
		if (btpSupportService == null) {
			return AjaxResult.error("服务不存在！");
		}
		if (btpSupportService.isEnabled()) {
			return AjaxResult.error("服务已为开启状态");
		}
		btpSupportService.setEnabled(true);
		btpSupportService.setUpdateBy(getUsername());
		btpSupportService.setUpdateTime(new Date());
		supportService.updateById(btpSupportService);
		return AjaxResult.success("开启服务成功!");
	}

	@PreAuthorize("@ss.hasPermi('business:support-service:open')")
	@Log(title = "服务管理", businessType = BusinessType.UPDATE)
	@PutMapping("{id}/off}")
	public AjaxResult closeService(@Validated @PathVariable Long id, @Validated @PathVariable String status) {
		BtpSupportService btpSupportService = supportService.getById(id);
		if (btpSupportService == null) {
			return AjaxResult.error("服务不存在！");
		}
		if (!btpSupportService.isEnabled()) {
			return AjaxResult.error("服务已为开启状态");
		}
		btpSupportService.setEnabled(false);
		btpSupportService.setUpdateBy(getUsername());
		btpSupportService.setUpdateTime(new Date());
		supportService.updateById(btpSupportService);
		return AjaxResult.success("关闭服务成功!");
	}

//	private void postEvent(BtpSupportService btpSupportService) {
//		eventBusCenter.postAsync(SupportServerEvent.builder() //
//				.id(btpSupportService.getId())//
//				.configuration(btpSupportService.getConfiguration())//
//				.product(btpSupportService.getProduct())//
//				.build());
//	}
}
