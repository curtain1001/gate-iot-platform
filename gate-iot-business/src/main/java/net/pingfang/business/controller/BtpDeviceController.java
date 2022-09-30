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

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.manager.DefaultDeviceOperatorManager;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.product.ProductSupports;

/**
 * @author 王超
 * @description 设备管理
 * @date 2022-06-29 23:28
 */
@RestController
@RequestMapping("business/device")
public class BtpDeviceController extends BaseController {

	@Resource
	IBtpDeviceService btpDeviceService;
	@Resource
	IBtpInstructionService instructionService;
	@Resource
	DefaultDeviceOperatorManager operatorManager;

	@GetMapping("/product/list")
	public AjaxResult getDeviceProduct() {
		return AjaxResult.success(ProductSupports.getDeviceSupports());
	}

	/**
	 * 分页列表
	 *
	 * @param device 通道信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:device:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpDevice device) {
		startPage();
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(device.getDeviceId()), BtpDevice::getDeviceId,
				device.getDeviceId());
		queryWrapper.like(StringUtils.checkValNotNull(device.getDeviceName()), BtpDevice::getDeviceName,
				device.getDeviceName());
		queryWrapper.like(StringUtils.checkValNotNull(device.getLaneId()), BtpDevice::getLaneId, device.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(device.getProduct()), BtpDevice::getProduct, device.getProduct());
		queryWrapper.orderByDesc(BtpDevice::getCreateTime);
		List<BtpDevice> list = btpDeviceService.list(queryWrapper);
		TableDataInfo info = getDataTable(list);
		info.setRows(info.getRows().stream().peek(x -> {
			((BtpDevice) x).setInstructions(
					instructionService.getInstructions(ProductSupports.getSupport(((BtpDevice) x).getProduct())));
		}).collect(Collectors.toList()));
		return info;
	}

	/**
	 * 分页列表
	 *
	 * @return 所有设备
	 */
	@GetMapping("/all/{laneId}")
	public AjaxResult getDevices(@PathVariable("laneId") Long laneId) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, laneId);
		List<BtpDevice> list = btpDeviceService.list(queryWrapper);
		list = list.stream().peek(x -> {
			x.setInstructions(
					instructionService.getInstructions(ProductSupports.getSupport(((BtpDevice) x).getProduct())));
		}).collect(Collectors.toList());
		return AjaxResult.success(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:device:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult load(@PathVariable Long id) {
		return AjaxResult.success(btpDeviceService.getById(id));
	}

	/**
	 * 根据设备号查询
	 */
	@GetMapping(value = "device-id/{deviceId}")
	public AjaxResult getDevice(@PathVariable String deviceId) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getDeviceId, deviceId);
		return AjaxResult.success(btpDeviceService.getOne(queryWrapper));
	}

	/**
	 * 根据设备名查询
	 */
	@GetMapping(value = "device-name/{deviceName}")
	public AjaxResult getLaneName(@PathVariable String deviceName) {
		LambdaQueryWrapper<BtpDevice> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpDevice::getDeviceName, deviceName);
		return AjaxResult.success(btpDeviceService.getOne(wrapper));
	}
	// TODO: 2022/8/1 同一车道允许存在多个设备
	// /**
	// * 根据通道号查询
	// */
	// @GetMapping(value = "/lane/{laneId}")
	// public AjaxResult getLaneNo(@PathVariable String laneId) {
	// LambdaQueryWrapper<BtpDevice> wrapper = Wrappers.lambdaQuery();
	// wrapper.eq(BtpDevice::getLaneId, laneId);
	// return AjaxResult.success(btpDeviceService.getOne(wrapper));
	// }

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:device:add')")
	@Log(title = "设备管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpDevice device) {
		LambdaQueryWrapper<BtpDevice> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpDevice::getDeviceId, device.getDeviceId());
		if (btpDeviceService.count(wrapper) > 0) {
			return AjaxResult.error("新增设备'" + device.getDeviceId() + "'失败，设备号已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpDevice::getDeviceName, device.getDeviceName());
		wrapper.eq(BtpDevice::getLaneId, device.getLaneId());
		if (btpDeviceService.count(wrapper) > 0) {
			return AjaxResult.error("新增设备'" + device.getDeviceName() + "'失败，设备名称已存在");
		}
		// wrapper = Wrappers.lambdaQuery();
		// wrapper.eq(BtpDevice::getLaneId, device.getLaneId());
		// wrapper.eq(BtpDevice::getProduct, device.getProduct());
		// if (btpDeviceService.count(wrapper) > 0) {
		// return AjaxResult.error("新增设备'" + device.getProduct() + "'失败，通道内设备产品唯一");
		// }
		device.setEnabled(1);
		device.setCreateBy(getUsername());
		device.setCreateTime(new Date());
		return toAjax(btpDeviceService.save(device));
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:device:edit')")
	@Log(title = "设备管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpDevice device) {
		LambdaQueryWrapper<BtpDevice> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpDevice::getDeviceName, device.getDeviceName());
		wrapper.eq(BtpDevice::getLaneId, device.getDeviceId());
		BtpDevice btpDevice = btpDeviceService.getOne(wrapper);
		if (btpDevice != null && !btpDevice.getId().equals(device.getId())) {
			return AjaxResult.error("修改设备'" + device.getDeviceName() + "'失败，设备名已存在");
		}
		device.setUpdateBy(getUsername());
		device.setUpdateTime(new Date());
		return toAjax(btpDeviceService.updateById(device));
	}

	/**
	 * 删除通道
	 */
	@PreAuthorize("@ss.hasPermi('business:device:remove')")
	@Log(title = "设备管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		Arrays.stream(ids).forEach(x -> {
			btpDeviceService.removeById(x);
		});
		return AjaxResult.success();
	}

	/**
	 * 开启设备
	 */
	@PreAuthorize("@ss.hasPermi('business:device:open')")
	@Log(title = "设备管理", businessType = BusinessType.UPDATE)
	@PutMapping("{id}/{status:(?:on|off)}")
	public AjaxResult openDevice(@Validated @PathVariable Long id, @Validated @PathVariable String status) {
		BtpDevice device = btpDeviceService.getById(id);
		if (device == null) {
			return AjaxResult.error("设备不存在！");
		}
		if ("on".equals(status)) {
			device.setEnabled(0);
			btpDeviceService.updateById(device);
			DeviceOperator deviceOperator = operatorManager.create(device.getLaneId(), device.getDeviceId(),
					ProductSupports.getSupport(device.getProduct()));
			if (deviceOperator != null) {
				return AjaxResult.success("开启设备成功");
			}
		}
		if ("off".equals(status)) {
			operatorManager.shutdown(device.getLaneId(), device.getDeviceId());
			device.setEnabled(1);
			btpDeviceService.updateById(device);
			return AjaxResult.success("关闭设备成功");
		}
		return AjaxResult.error("on".equals(status) ? "开启设备失败" : "关闭设备失败");
	}
}
