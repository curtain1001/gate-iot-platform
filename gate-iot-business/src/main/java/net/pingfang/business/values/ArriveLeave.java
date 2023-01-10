package net.pingfang.business.values;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 0:19
 */
@Data
@Builder
@AllArgsConstructor
public class ArriveLeave {
	/**
	 * 箱区
	 */
	private long areaNo;
	/**
	 * 时间
	 */
	private Date dateTime;
	/**
	 * 进出闸类型
	 */
	private String inExitFlag;
	/**
	 * 车道号
	 */
	private String laneNo;
	/**
	 * 消息类型
	 */
	private String messageType;

}
