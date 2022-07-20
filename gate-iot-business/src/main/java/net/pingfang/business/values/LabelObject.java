package net.pingfang.business.values;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description 级联选择对象
 * @date 2022-07-05 9:46
 */

@Data
@Builder(toBuilder = true)
public class LabelObject {
	Object value;
	String label;
	List<LabelObject> children;
}
