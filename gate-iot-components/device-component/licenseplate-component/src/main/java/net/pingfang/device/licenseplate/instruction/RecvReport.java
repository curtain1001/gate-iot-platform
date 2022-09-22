package net.pingfang.device.licenseplate.instruction;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.instruction.DeviceInstruction;
import net.pingfang.device.licenseplate.LicensePlateProduct;
import net.pingfang.device.licenseplate.values.ImageRecvInfo;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;

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
	public Product getProduct() {
		return LicensePlateProduct.OCR_License_Plate;
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.up;
	}

//	@Override
//	public Flux<FunctionMessage> subscribe(Long laneId) {
//		return processor.map(Function.identity()).cast(FunctionMessage.class).filterWhen(x -> {
//			if (laneId != null) {
//				return Mono.just(laneId.equals(x.getLaneId()));
//			} else {
//				return Mono.just(true);
//			}
//		});
//	}

	@Override
	public boolean isSupport(Object object) {
		if (object instanceof ImageRecvInfo) {
			return true;
		} else {
			return false;
		}
	}
//
//	@Override
//	public void received(FunctionMessage functionMessage) {
//		if (processor.getPending() > processor.getBufferSize() / 2) {
//			log.warn(" message pending {} ,drop message:{}", processor.getPending(), functionMessage.toString());
//			return;
//		}
//		sink.next(functionMessage);
//	}
}
