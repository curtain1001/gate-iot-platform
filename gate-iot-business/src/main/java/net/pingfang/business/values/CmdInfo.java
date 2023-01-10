package net.pingfang.business.values;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 0:56
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CmdInfo {
	String commandCode;
	String commandName;
	List<CmdValue> parameter;
	String recipientCode;
	Date sendTime;
	String senderCode;
}
