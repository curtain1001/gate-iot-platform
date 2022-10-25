package net.pingfang.business.values;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description 键值对对象
 * @date 2022-07-05 9:54
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class KeyValuePair {
	final Object key;
	final Object value;
}
