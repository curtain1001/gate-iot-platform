package net.pingfang.business.values;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-18 15:19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Topic {
	private String topic;
	private String payload;
}
