package net.pingfang.containerocr.instruction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.dom.DomUtils;
import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.instruction.InstructionResult;
import net.pingfang.iot.common.instruction.InstructionType;
import net.pingfang.iot.common.instruction.ObjectType;
import net.pingfang.iot.common.product.Product;
import net.pingfang.servicecomponent.core.BusinessInstruction;

/**
 * <p>
 * 箱号识别指令
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 14:09
 */
@Slf4j
public class OrcInstruction implements BusinessInstruction {
	@Override
	public String getName() {
		return "箱号识别上报";
	}

	@Override
	public String getValue() {
		return "CONTAINER_OCR";
	}

	@Override
	public Product getProduct() {
		return ContainerOcrProduct.CONTAINER_OCR;
	}

	@Override
	public InstructionType getInsType() {
		return InstructionType.up;
	}

	@Override
	public ObjectType getObjectType() {
		return BusinessInstruction.super.getObjectType();
	}

	@Override
	public boolean isSupport(NetworkMessage networkMessage) {
		String message = networkMessage.payloadAsString();
		if (message.endsWith("</Vehicle>")) {
			Document str = null;
			try {
				str = DomUtils.getXMLByString(message);
			} catch (DocumentException e) {
				log.error("xml报文解析错误：", e);
			}
			if (str != null) {
				Element element = str.getRootElement();
				if (element != null && element.element("System") != null
						&& "CR+CDI".equals(element.element("System").getText())) {
					log.info("箱号识别报文：{}", networkMessage.payloadAsString());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public NetworkMessage decode(NetworkMessage networkMessage) {
		String message = networkMessage.payloadAsString();
		if (message.endsWith("</Vehicle>")) {
			Document str = null;
			try {
				str = DomUtils.getXMLByString(message);
			} catch (DocumentException e) {
				log.error("xml报文解析错误：", e);
			}
			if (str != null) {
				Element element = str.getRootElement();
				if (element != null && element.element("System") != null
						&& "CR+CDI".equals(element.element("System").getText())) {
					return networkMessage.toBuilder().payload(message).payloadType(MessagePayloadType.STRING).build();
				}
			}
		}
		return networkMessage;
	}

	@Override
	public InstructionResult execution(JsonNode jsonNode) {
		return null;
	}
}
