package net.pingfang.business.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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

import net.pingfang.business.domain.BtpNetworkConfig;
import net.pingfang.business.domain.BtpSupportServer;
import net.pingfang.business.service.IBtpNetworkConfigService;
import net.pingfang.business.service.IBtpSupportServerService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.servicecomponent.event.SupportServerEvent;

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
public class BtpSupportServerController extends BaseController {
	@Resource
	public IBtpSupportServerService supportServerService;
	@Resource
	public IBtpNetworkConfigService networkConfigService;
	@Resource
	public EventBusCenter eventBusCenter;

	@GetMapping("/product/list")
	public AjaxResult getServerSupports() {
		return AjaxResult.success(ProductSupports.getServerSupports());
	}

	/**
	 * 分页列表
	 *
	 * @param supportServer 支撑服务信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpSupportServer supportServer) {
		startPage();
		LambdaQueryWrapper<BtpSupportServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(supportServer.getProduct()), BtpSupportServer::getProduct,
				supportServer.getProduct());
		queryWrapper.orderByDesc(BtpSupportServer::getCreateTime);
		List<BtpSupportServer> list = supportServerService.list(queryWrapper);
		TableDataInfo info = getDataTable(list);
		info.setRows(info.getRows().stream().peek(x -> {
			BtpSupportServer server = (BtpSupportServer) x;
			LambdaQueryWrapper<BtpNetworkConfig> lambdaQueryWrapper = Wrappers.lambdaQuery();
			lambdaQueryWrapper.eq(BtpNetworkConfig::getNetworkConfigId, server.getNetworkId());
			BtpNetworkConfig networkConfig = networkConfigService.getOne(lambdaQueryWrapper);
			if (networkConfig != null) {
				server.setNetworkName(networkConfig.getName());
			}
		}).collect(Collectors.toList()));
		return getDataTable(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult load(@PathVariable Long id) {
		return AjaxResult.success(supportServerService.getById(id));
	}

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:add')")
	@Log(title = "支撑服务配置", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpSupportServer supportServer) {
		LambdaQueryWrapper<BtpSupportServer> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpSupportServer::getProduct, supportServer.getProduct());
		if (supportServerService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务配置'" + supportServer.getProduct() + "'失败，该服务配置已存在");
		}
		supportServer.setCreateBy(getUsername());
		supportServer.setCreateTime(new Date());
		postEvent(supportServer);
		return toAjax(supportServerService.save(supportServer));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:support-service:edit')")
	@Log(title = "支撑服务配置", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpSupportServer supportServer) {
		LambdaQueryWrapper<BtpSupportServer> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpSupportServer::getProduct, supportServer.getProduct());
		BtpSupportServer btpSupportServer = supportServerService.getOne(wrapper);
		if (btpSupportServer != null && !btpSupportServer.getId().equals(supportServer.getId())) {
			return AjaxResult.error("修改配置'" + supportServer.getProduct() + "'失败，服务配置已存在");
		}
		supportServer.setUpdateBy(getUsername());
		supportServer.setUpdateTime(new Date());
		postEvent(supportServer);
		return toAjax(supportServerService.updateById(supportServer));
	}

	private void postEvent(BtpSupportServer supportServer) {
		eventBusCenter.postAsync(SupportServerEvent.builder() //
				.id(supportServer.getId())//
				.configuration(supportServer.getConfiguration())//
				.product(supportServer.getProduct())//
				.build());
	}
}
