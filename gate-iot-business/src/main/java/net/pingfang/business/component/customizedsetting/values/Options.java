package net.pingfang.business.component.customizedsetting.values;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-24 18:04
 */
@Data
@Builder
public class Options {
	final String key;
	final String value;
}
