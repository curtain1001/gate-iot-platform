package net.pingfang.device.licenseplate.instruction;

import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.licenseplate.LicensePlateDevice;
import net.pingfang.device.licenseplate.LicensePlateProduct;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-19 22:32
 */
public class ImageSnap implements DeviceInstruction {
	@Override
	public String getName() {
		return "强制抓拍";
	}

	@Override
	public String getValue() {
		return "IMAGE_SNAP";
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.down;
	}

	@Override
	public Product getProduct() {
		return LicensePlateProduct.OCR_License_Plate;
	}

	@Override
	public Mono<InstructionResult<Object, String>> execution(DeviceOperator deviceOperator) {
		return Mono.fromCallable(() -> {
			LicensePlateDevice device = (LicensePlateDevice) deviceOperator;
			return device.imageSnap();
		}).map(x -> InstructionResult.success(true, x));
	}
}
