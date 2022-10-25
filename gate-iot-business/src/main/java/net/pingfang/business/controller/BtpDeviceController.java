package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.business.service.IBtpSupportServiceService;
import net.pingfang.business.values.NetworkTypeInfo;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.device.core.DeviceManager;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.iot.common.network.NetworkTypes;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.iot.common.product.DeviceProductSupports;
import net.pingfang.network.NetworkManager;

/**
 * @author 王超
 * @description 设备管理
 * @date 2022-06-29 23:28
 */
@RestController
@RequestMapping("business/device")
@Slf4j
public class BtpDeviceController extends BaseController {

	@Resource
	IBtpDeviceService btpDeviceService;
	@Resource
	IBtpInstructionService instructionService;
	@Resource
	NetworkManager networkManager;

	@Resource
	DeviceManager deviceManager;

	@Resource
	IBtpSupportServiceService supportService;

	@GetMapping("/product/list")
	public AjaxResult getDeviceProduct() {
		return AjaxResult.success(DeviceProductSupports.getDeviceSupports());
	}

	/**
	 * 获取支持的网络组件类型
	 *
	 * @return
	 */
	@GetMapping("/network/supports")
	public AjaxResult getSupports() {
		return AjaxResult.success(networkManager.getProviders().stream().map(network -> {
			return NetworkTypeInfo.of(network.getType(), network.getType().getBasicForm());
		}).collect(Collectors.toList()));
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
			Optional<DeviceProduct> deviceProduct = DeviceProductSupports.lookup(((BtpDevice) x).getProduct());
			deviceProduct
					.ifPresent(product -> ((BtpDevice) x).setInstructions(instructionService.getInstructions(product)));
		}).collect(Collectors.toList()));
		return info;
	}

	/**
	 * 获取该车道所有设备
	 *
	 * @return 所有设备
	 */
	@GetMapping("/all/{laneId}")
	public AjaxResult getDevices(@PathVariable("laneId") Long laneId) {
		LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpDevice::getLaneId, laneId);
		List<BtpDevice> list = btpDeviceService.list(queryWrapper);
		list = list.stream().peek(x -> {
			Optional<DeviceProduct> deviceProduct = DeviceProductSupports.lookup(x.getProduct());
			deviceProduct.ifPresent(product -> x.setInstructions(instructionService.getInstructions(product)));
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
	 * 新增设备
	 */
	@PreAuthorize("@ss.hasPermi('business:device:add')")
	@Log(title = "设备管理", businessType = BusinessType.INSERT)
	@PostMapping
	@Transactional
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
		Optional<NetworkType> networkType = NetworkTypes.lookup(device.getNetworkType());
		if (!networkType.isPresent()) {
			return AjaxResult.error("找不到该网络组件");
		}
		Optional<DeviceProduct> product = DeviceProductSupports.lookup(device.getProduct());
		if (!product.isPresent() || !product.get().getNetwork().contains(networkType.get())) {
			return AjaxResult.error("新建失败：所选网络组件不符合设备连接要求");
		}
		device.setEnabled(true);
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
			device.setEnabled(true);
			btpDeviceService.updateById(device);
			Optional<DeviceProduct> p = DeviceProductSupports.lookup(device.getProduct());
			if (p.isPresent()) {
				DeviceOperator deviceOperator = deviceManager.create(device.getLaneId(), device.getDeviceId(), p.get());
				if (deviceOperator != null) {
					return AjaxResult.success("开启设备成功");
				}
			}
		}
		if ("off".equals(status)) {
			deviceManager.shutdown(device.getLaneId(), device.getDeviceId());
			device.setEnabled(false);
			btpDeviceService.updateById(device);
			return AjaxResult.success("关闭设备成功");
		}
		return AjaxResult.error("on".equals(status) ? "开启设备失败" : "关闭设备失败");
	}
}
