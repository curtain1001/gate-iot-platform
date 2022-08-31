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

import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.domain.BtpNetworkConfig;
import net.pingfang.business.enums.NetworkConfigState;
import net.pingfang.business.service.IBtpNetworkConfigService;
import net.pingfang.business.values.NetworkTypeInfo;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.utils.SecurityUtils;
import net.pingfang.network.NetworkManager;

/**
 * <p>
 * 网络组件工具
 * </p>
 *
 * @author 王超
 * @since 2022-08-24 11:02
 */
@Slf4j
@RequestMapping("/business/network")
@RestController
public class BtpNetworkConfigController extends BaseController {

	@Resource
	NetworkManager networkManager;

	@Resource
	IBtpNetworkConfigService configService;

	/**
	 * 分页列表
	 *
	 * @param config 网络组件信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:network:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpNetworkConfig config) {
		startPage();
		LambdaQueryWrapper<BtpNetworkConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(config.getNetworkConfigId()),
				BtpNetworkConfig::getNetworkConfigId, config.getNetworkConfigId());
		queryWrapper.like(StringUtils.checkValNotNull(config.getType()), BtpNetworkConfig::getType, config.getType());
		queryWrapper.like(StringUtils.checkValNotNull(config.getStatus()), BtpNetworkConfig::getStatus,
				config.getStatus());
		queryWrapper.orderByDesc(BtpNetworkConfig::getCreateTime);
		List<BtpNetworkConfig> list = configService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 获取指定网络类型组件
	 *
	 * @param type 网络类型
	 * @return 分页数据
	 */
	@GetMapping("/list/{type}")
	public AjaxResult getList(@PathVariable String type) {
		LambdaQueryWrapper<BtpNetworkConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(BtpNetworkConfig::getType, type);
		queryWrapper.like(BtpNetworkConfig::getStatus, NetworkConfigState.enabled);
		queryWrapper.orderByDesc(BtpNetworkConfig::getCreateTime);
		List<BtpNetworkConfig> list = configService.list(queryWrapper);
		return AjaxResult.success(list);
	}

	/**
	 * 获取支持的网络组件类型
	 *
	 * @return
	 */
	@GetMapping("/supports")
	public AjaxResult getSupports() {
		return AjaxResult.success(networkManager.getProviders().stream().map(network -> {
			return NetworkTypeInfo.of(network.getType(), network.getBasicForm());
		}).collect(Collectors.toList()));
	}

	@PostMapping
	@PreAuthorize("@ss.hasPermi('business:network:add')")
	public AjaxResult create(@Validated @RequestBody BtpNetworkConfig config) {
		config.setStatus(NetworkConfigState.disabled);
		config.setCreateBy(SecurityUtils.getUsername());
		config.setCreateTime(new Date());
		return toAjax(configService.save(config));
	}

	@GetMapping("{id}")
	public AjaxResult load(@Validated @PathVariable("id") Long id) {
		return AjaxResult.success(configService.getById(id));
	}

	/**
	 * 启动网络组件
	 *
	 * @param id 网络组件ID
	 * @return
	 */
	@PutMapping("/{id}/start")
	public AjaxResult start(@PathVariable String id) {
		BtpNetworkConfig config = configService.getById(id);
		if (config == null) {
			return AjaxResult.error("网络配置不存在 id:" + id);
		}
		config = config.toBuilder() //
				.status(NetworkConfigState.enabled) //
				.build();
		configService.updateById(config);
		try {
			networkManager.reload(config.lookupNetworkType(), config.getNetworkConfigId());
		} catch (Exception e) {
			log.error("网络组件启动失败：", e);
			config = config.toBuilder() //
					.status(NetworkConfigState.disabled) //
					.build();
			configService.updateById(config);
			return AjaxResult.error("网络组件启动失败：" + e.getMessage());
		}
		return AjaxResult.success("启动成功");
	}

	/**
	 * 停止网络组件
	 *
	 * @param id
	 * @return
	 */
	@PutMapping("/{id}/shutdown")
	public AjaxResult shutdown(@PathVariable String id) {
		BtpNetworkConfig config = configService.getById(id);
		if (config == null) {
			return AjaxResult.error("网络配置不存在 id:" + id);
		}
		config = config.toBuilder() //
				.status(NetworkConfigState.disabled) //
				.build();
		configService.updateById(config);
		try {
			networkManager.shutdown(config.lookupNetworkType(), id);
		} catch (Exception e) {
			log.error("网络组件关闭失败：", e);
			return AjaxResult.error("网络组件关闭失败：" + e.getMessage());
		}
		return AjaxResult.success("启动成功");
	}

	/**
	 * 删除参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:network:remove')")
	@Log(title = "网络组件管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		configService.removeByIds(Arrays.asList(ids));
		return success();
	}

}
