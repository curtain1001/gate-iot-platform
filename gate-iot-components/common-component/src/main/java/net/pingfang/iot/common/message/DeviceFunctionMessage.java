package net.pingfang.iot.common.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import net.pingfang.common.utils.uuid.UUID;
import net.pingfang.iot.common.instruction.Instruction;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 19:23
 */
@Data
public class DeviceFunctionMessage implements DeviceMessage {
	final String messageId;
	final long laneId;
	final String deviceId;
	final String instruction;
	final String funType;
	final String payload;
	final String date;

	public DeviceFunctionMessage(String deviceId, long laneId, Instruction instruction, FunType funType, String content,
			Date date) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.messageId = UUID.randomUUID().toString(true);
		this.instruction = instruction.getValue();
		this.payload = content;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		this.date = dateFormat.format(date);
		this.funType = funType.name();
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
