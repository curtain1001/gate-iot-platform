package net.pingfang.iot.common.message;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 19:17
 */
public interface DeviceMessage {
	String getDeviceId();

	long getLaneId();

	String getMessageId();

	String getDate();

	String getPayload();
}
