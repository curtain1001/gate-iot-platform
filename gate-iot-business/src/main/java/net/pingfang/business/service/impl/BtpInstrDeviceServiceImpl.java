package net.pingfang.business.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpInstrDevice;
import net.pingfang.business.manager.DefaultInstructionManager;
import net.pingfang.business.mapper.BtpInstrDeviceMapper;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpInstrDeviceService;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.ObjectType;

/**
 * @author 王超
 * @description 设备命令关联信息服务实现类
 * @date 2022-06-29 0:17
 */
@Service
public class BtpInstrDeviceServiceImpl extends ServiceImpl<BtpInstrDeviceMapper, BtpInstrDevice>
		implements IBtpInstrDeviceService {
	@Resource
	BtpInstrDeviceMapper instrDeviceMapper;
	@Resource
	IBtpInstructionService instructionService;
	@Resource
	DefaultInstructionManager instructionManager;
	@Resource
	IBtpDeviceService deviceService;

	@Override
	public List<Instruction> getInstructions(String deviceId) {
		LambdaQueryWrapper<BtpDevice> btpDeviceLambdaQueryWrapper = Wrappers.lambdaQuery();
		btpDeviceLambdaQueryWrapper.eq(BtpDevice::getDeviceId, deviceId);
		BtpDevice d = deviceService.getOne(btpDeviceLambdaQueryWrapper);
		Map<String, Instruction> instructionMap = instructionManager.getIns(ObjectType.device);
		return instructionMap.values().stream()
				.filter(x -> ((DeviceInstruction) x).getProduct().isSupported(d.getProduct()))
				.collect(Collectors.toList());
	}
}
