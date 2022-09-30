package net.pingfang.device.licenseplate.instruction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.common.utils.StringUtils;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.licenseplate.LicensePlateDevice;
import net.pingfang.device.licenseplate.LicensePlateProduct;
import net.pingfang.device.licenseplate.values.StatusCode;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.customizedsetting.values.DefaultCustomized;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.manager.LaneConfigManager;
import net.pingfang.iot.common.product.Product;

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
	public InstructionResult<Object, String> execution(DeviceOperator deviceOperator, Map<String, Object> properties,
			JsonNode jsonNode) {
		LicensePlateDevice device = (LicensePlateDevice) deviceOperator;
		String url = Injection.manager.getConfig(DefaultCustomized.PICTURE_STORE_DIRECTORY, deviceOperator.getLaneId());
		if (StringUtils.isEmpty(url)) {
			url = System.getProperty("java.io.tmpdir");
		}
		File file = new File(url);
		if (!file.exists()) {
			file.mkdir();
		}
		url = !url.endsWith("/") && !url.endsWith("\\") ? url + "\\" : url;
		StringBuilder sbf = new StringBuilder(url);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateName = dateFormat.format(new Date());
		sbf.append("capture_");
		sbf.append(dateName);
		sbf.append(".jpeg");
		String imgPath = sbf.toString().replaceAll("\\\\", "/");
		int is = device.saveImageToJpeg(imgPath);
		if (is == 0) {
			return InstructionResult.success(sbf.toString(), "执行成功，文件路径为：" + imgPath);
		} else {
			return InstructionResult.success(sbf.toString(),
					"执行失败：" + StatusCode.getStatusCode(is, "Net_SaveImageToJpeg"));
		}
	}

	@Override
	public boolean isSupport(NetworkMessage networkMessage) {
		return false;
	}

	@Component
	private static class Injection {

		private static LaneConfigManager manager;

		public Injection(LaneConfigManager manager) {
			Injection.manager = manager;
		}
	}

}
