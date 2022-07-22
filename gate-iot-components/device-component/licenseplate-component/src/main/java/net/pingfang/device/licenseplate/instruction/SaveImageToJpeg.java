package net.pingfang.device.licenseplate.instruction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.core.manager.LaneConfigManager;
import net.pingfang.device.licenseplate.LicensePlateDevice;
import net.pingfang.device.licenseplate.LicensePlateProduct;
import net.pingfang.iot.common.customizedsetting.values.DefaultCustomized;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 16:32
 */
public class SaveImageToJpeg implements DeviceInstruction {

	@Override
	public String getName() {
		return "图片抓拍";
	}

	@Override
	public String getValue() {
		return "IMAGE_CAPTURE";
	}

	@Override
	public ObjectType getObjectType() {
		return ObjectType.device;
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
			String url = Injection.manager.getConfig(DefaultCustomized.PICTURE_STORE_DIRECTORY,
					deviceOperator.getLaneId());
			File file = new File(url);
			if (!file.exists()) {
				file.mkdir();
			}
			StringBuilder sbf = new StringBuilder(url);
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateName = dateFormat.format(new Date());
			sbf.append("capture_");
			sbf.append(dateName);
			sbf.append(".jpeg");
			return device.saveImageToJpeg(sbf.toString());
		}).map(x -> InstructionResult.success(x, x + ";图片保存地址为：" + x));

	}

	@Component
	private static class Injection {

		private static LaneConfigManager manager;

		public Injection(LaneConfigManager manager) {
			Injection.manager = manager;
		}
	}

}
