package net.pingfang.business.values;

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
 * @since 2022-12-20 14:07
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModuleSupportValue {
	/**
	 * 模块名
	 */
	String name;
	/**
	 * 模块代码
	 */
	String code;
	/**
	 * 模块设备限制
	 */
	List<String> restrictionType;

}
