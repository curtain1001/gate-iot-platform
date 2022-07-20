package net.pingfang.iot.common.event;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.FunctionMessage;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-15 18:06
 */
@Data
@Builder
public class FunctionUpEvent {
	Date time;
	FunctionMessage functionMessage;
}
