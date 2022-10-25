package net.pingfang.device.licenseplate.instruction;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import net.pingfang.device.licenseplate.LicensePlateDeviceProduct;
import net.pingfang.iot.common.instruction.Instruction;
import net.pingfang.iot.common.instruction.InstructionProvider;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-18 9:13
 */
@Component
public class LPInstructionProvider implements InstructionProvider {
	@Override
	public String getName() {
		return LicensePlateDeviceProduct.OCR_LICENSE_PLATE_III.name();
	}

	@Override
	public List<Instruction> getCommand() {
		return Arrays.asList( //
				new RecvReport(), // 识别上报
				new SaveImageToJpeg(), // 图片抓拍
				new ImageSnap()); // 强制抓拍
	}
}
