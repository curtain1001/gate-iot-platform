package net.pingfang.common.utils;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 对象字段工具
 * </p>
 *
 * @author 王超
 * @since 2022-09-22 14:34
 */
@Data
@Builder(toBuilder = true)
public class Column {
	/**
	 * 字段属性名
	 */
	final String columnValue;
	/**
	 * 字段中文属性含义
	 */
	final String columnName;
}
