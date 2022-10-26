package net.pingfang.iot.common.message;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.pingfang.common.utils.uuid.UUID;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 19:38
 */
@Data
@AllArgsConstructor
public class DeviceCollectMessage implements DeviceMessage, Serializable {
	private String messageId;
	private long laneId;
	private String deviceId;
	private String payload;
	private String date;

	public DeviceCollectMessage(String deviceId, long laneId, String content, Date date) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.messageId = UUID.randomUUID().toString(true);
		this.payload = content;
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
