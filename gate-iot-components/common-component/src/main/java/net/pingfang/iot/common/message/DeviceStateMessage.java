package net.pingfang.iot.common.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import net.pingfang.common.utils.uuid.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 19:20
 */
@Data
public class DeviceStateMessage implements DeviceMessage {
	final String messageId;
	final long laneId;
	final String deviceId;
	final String payload;
	final String date;

	public DeviceStateMessage(String deviceId, long laneId, DeviceState state, Date date) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.messageId = UUID.randomUUID().toString(true);
		this.payload = state.name();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		this.date = dateFormat.format(date);
	}

	@Override
	public String getDeviceId() {
		return this.deviceId;
	}

	@Override
	public long getLaneId() {
		return this.laneId;
	}

	@Override
	public String getMessageId() {
		return this.messageId;
	}

	@Override
	public String getDate() {
		return this.date;
	}

	@Override
	public String getPayload() {
		return this.payload;
	}
}
