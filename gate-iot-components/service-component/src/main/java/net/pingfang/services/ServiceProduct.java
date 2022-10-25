package net.pingfang.services;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-14 14:40
 */
@Getter
@AllArgsConstructor
public enum ServiceProduct {
	LANE_CONTROL("车道控制服务"), //
	CONTAINER_OCR("箱号识别服务"), //
	WHEEL_HUB_OCR("车轮毂识别服务"), //
	DAMAGED_OCR("残损识别服务"), //
	FRAME_NUM_OCR("车架号识别服务"), //
	LED_HINT("LED屏提示服务"), //
	LICENSE_PLATE_OCR("车牌识别服务"), //
	IC_OCR("IC卡识别服务"), //
	RFID_OCR("RFID识别服务"), //
	CUSTOMS_LOCK_CONTROL("关锁服务"), //
	WEIGHBRIDGE("称重服务"), //
	VOICE_BROADCAST("语音播报服务"),//
	;

	public final String name;
	static {
		ServiceProductSupports.register(Arrays.asList(ServiceProduct.values()));
	}
}
