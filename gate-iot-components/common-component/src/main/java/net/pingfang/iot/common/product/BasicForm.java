package net.pingfang.iot.common.product;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-19 17:36
 */
@Data
@Builder
public class BasicForm {
	final String field;
	final String type;

	enum Type {
		String,
	}
}
