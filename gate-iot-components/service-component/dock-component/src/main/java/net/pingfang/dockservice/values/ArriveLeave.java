package net.pingfang.dockservice.values;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 车辆到达离开报文
 * </p>
 *
 * @author 王超
 * @since 2022-10-10 9:33
 */
@Data
@Builder(toBuilder = true)
public class ArriveLeave {
    /**
     * 场站号
     */
    final String areaNo;
    /**
     * 时间
     */
    final String dateTime;

    /**
     * 出入闸类型
     */
    final String inExitFlag;

    /**
     * 通道号
     */
    final String laneNo;
    /**
     * 消息类型
     */
    final String messageType;
}
