package net.pingfang.business.values;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 0:56
 */
@Data
@Builder
public class CmdValue {
	String cmdtype;
	String cmdvalue;
}
