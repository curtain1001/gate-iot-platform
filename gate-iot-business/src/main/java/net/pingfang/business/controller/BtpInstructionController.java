package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
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
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpInstruction;
import net.pingfang.business.manager.DefaultDeviceOperatorManager;
import net.pingfang.business.manager.DefaultInstructionManager;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpInstrDeviceService;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.manager.AsyncManager;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.iot.common.instruction.InstructionParam;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-18 10:34
 */
@RestController
@RequestMapping("business/instruction")
@Slf4j
public class BtpInstructionController extends BaseController {
	@Resource
	private IBtpInstructionService instructionService;

	@Resource
	private IBtpInstrDeviceService instrDeviceService;

	@Resource
	private DefaultInstructionManager instructionManager;

	@Resource
	private IBtpDeviceService deviceService;

	@Resource
	private DefaultDeviceOperatorManager operatorManager;

	/**
	 * 分页列表
	 *
	 * @param instruction 指令信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpInstruction instruction) {
		startPage();
		LambdaQueryWrapper<BtpInstruction> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(instruction.getCommandName()), BtpInstruction::getCommandName,
				instruction.getCommandName());
		queryWrapper.like(StringUtils.checkValNotNull(instruction.getProduct()), BtpInstruction::getProduct,
				instruction.getProduct());
		queryWrapper.like(StringUtils.checkValNotNull(instruction.getType()), BtpInstruction::getType,
				instruction.getType());
		List<BtpInstruction> list = instructionService.list(queryWrapper);
		return getDataTable(list);
	}

	/**
	 * 执行指令
	 */
	@PreAuthorize("@ss.hasPermi('business:device:open')")
	@Log(title = "指令管理", businessType = BusinessType.UPDATE)
	@GetMapping("{id}/run/{product}/{instruction}")
	public AjaxResult run(@Validated @PathVariable Long id, @PathVariable String product,
			@Validated @PathVariable String instruction) {
		DeviceInstruction i = (DeviceInstruction) instructionManager.getInstruction(instruction);
		BtpDevice device = deviceService.getById(id);
		if (device == null) {
			return AjaxResult.error("设备不存在");
		}
		DeviceOperator operator = operatorManager.getDevice(device.getLaneId(), device.getDeviceId());
		if (operator == null) {
			return AjaxResult.error("设备未启动");
		}
		InstructionResult<Object, String> resultMono = i.execution(operator, Maps.newHashMap(),
				JsonUtils.getObjectNode());
		if (resultMono == null) {
			return AjaxResult.error("指令执行失败");
		}
		if (resultMono.isSuccess()) {
			return AjaxResult.success("指令执行成功:" + resultMono.getMessage());
		} else {
			return AjaxResult.error("指令执行失败:" + resultMono.getMessage());
		}
	}

	/**
	 * 执行指令
	 */
	@PreAuthorize("@ss.hasPermi('business:device:open')")
	@Log(title = "指令管理", businessType = BusinessType.UPDATE)
	@PutMapping("exec")
	public AjaxResult run(InstructionParam param) {
		try {
			InstructionResult result = instructionManager.exec(param);
			if (result == null) {
				return AjaxResult.error("指令执行失败");
			}
			if (result.isSuccess()) {
				return AjaxResult.success("指令执行成功:" + result.getMessage());
			} else {
				return AjaxResult.error("指令执行失败:" + result.getMessage());
			}
		} catch (Exception e) {
			log.error("指令执行失败;", e);
			return AjaxResult.error("指令执行失败：" + e.getMessage());
		}
	}

	/**
	 * 获取服务列表
	 */
	@GetMapping("list/{objetType}")
	public AjaxResult getInstructions(@Validated @PathVariable("objetType") String objetType) {
		return AjaxResult.success(instructionManager.getInstruction().stream()
				.filter(x -> ObjectType.valueOf(objetType) == x.getObjectType()).collect(Collectors.toList()));
	}

	/**
	 * 根据参数编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:query')")
	@GetMapping(value = "/{instructionId}")
	public AjaxResult getInfo(@PathVariable Long instructionId) {
		return AjaxResult.success(instructionService.getById(instructionId));
	}

	/**
	 * 根据参数键名查询参数值
	 */
	@GetMapping(value = "/instruction/{commandValue}")
	public AjaxResult getInstructionName(@PathVariable String commandValue) {
		LambdaQueryWrapper<BtpInstruction> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpInstruction::getCommandValue, commandValue);
		return AjaxResult.success(instructionService.getOne(wrapper));
	}

	/**
	 * 新增场站信息
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:add')")
	@Log(title = "指令管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpInstruction instruction) {
		LambdaQueryWrapper<BtpInstruction> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpInstruction::getProduct, instruction.getProduct());
		wrapper.eq(BtpInstruction::getCommandValue, instruction.getCommandValue());
		if (instructionService.count(wrapper) > 0) {
			return AjaxResult.error("新增指令'" + instruction.getCommandValue() + "'失败，指令键值已存在");
		}
		instruction.setCreateBy(getUsername());
		instruction.setCreateTime(new Date());
		boolean bln = instructionService.save(instruction);
		// 提交异步刷新指令任务
		refreshIns(instruction.getProduct());
		return toAjax(bln);
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:edit')")
	@Log(title = "指令管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpInstruction instruction) {
		LambdaQueryWrapper<BtpInstruction> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpInstruction::getProduct, instruction.getProduct());
		wrapper.eq(BtpInstruction::getCommandValue, instruction.getCommandValue());
		BtpInstruction BtpInstruction = instructionService.getOne(wrapper);
		if (BtpInstruction != null && !BtpInstruction.getInstrId().equals(instruction.getInstrId())) {
			return AjaxResult.error("修改指令'" + instruction.getCommandValue() + "'失败，指令键值已存在");
		}
		instruction.setUpdateBy(getUsername());
		instruction.setUpdateTime(new Date());
		boolean bln = instructionService.updateById(instruction);
		// 提交异步刷新指令任务
		refreshIns(instruction.getProduct());
		return toAjax(bln);
	}

	/**
	 * 删除参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:remove')")
	@Log(title = "指令管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{instructionIds}")
	public AjaxResult remove(@PathVariable Long[] instructionIds) {
		instructionService.removeByIds(Arrays.asList(instructionIds));
		refreshIns("");
		return success();
	}

	private void refreshIns(String product) {
		// 提交异步刷新指令任务
		AsyncManager.me().execute(new TimerTask() {
			@Override
			public void run() {
				instructionManager.refresh(product);
			}
		});
	}

	/**
	 * 开启设备
	 */
	@PreAuthorize("@ss.hasPermi('business:instruction:open')")
	@Log(title = "指令管理", businessType = BusinessType.UPDATE)
	@PutMapping("{id}/{status:(?:on|off)}")
	public AjaxResult openInstr(@Validated @PathVariable Long id, @Validated @PathVariable String status) {
		BtpInstruction instruction = instructionService.getById(id);
		if (instruction == null) {
			return AjaxResult.error("指令不存在！");
		}
		instruction = instruction.toBuilder() //
				.status("on".equals(status) ? 0 : 1) //
				.build();
		instructionService.updateById(instruction);
		refreshIns(instruction.getProduct());
		return success();
	}

}
