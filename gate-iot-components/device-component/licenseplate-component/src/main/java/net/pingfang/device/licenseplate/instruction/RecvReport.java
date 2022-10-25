package net.pingfang.device.licenseplate.instruction;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.licenseplate.LicensePlateDeviceProduct;
import net.pingfang.device.licenseplate.values.ImageRecvInfo;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.DeviceProduct;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 15:46
 */
@Slf4j
public class RecvReport implements DeviceInstruction {

	@Override
	public String getName() {
		return "车牌识别上报";
	}

	@Override
	public String getValue() {
		return "RECV_REPORT";
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.device;
	}

	@Override
	public DeviceProduct getProduct() {
		return LicensePlateDeviceProduct.OCR_LICENSE_PLATE_III;
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.up;
	}

	@Override
	public boolean isSupport(NetworkMessage networkMessage) {
		try {
			JsonUtils.convert(networkMessage.getPayload(), ImageRecvInfo.class);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
