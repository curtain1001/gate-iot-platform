package net.pingfang.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpInstrDevice;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.product.Product;

/**
 * @author 王超
 * @description 设备命令关联信息服务
 * @date 2022-06-29 0:16
 */
public interface IBtpInstrDeviceService extends IService<BtpInstrDevice> {

	List<Instruction> getInstructions(Product product);
}
