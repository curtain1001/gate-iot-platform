package net.pingfang.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.utils.Lists;

import net.pingfang.common.annotation.FieldName;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-22 11:56
 */
public class FieldNameUtils {
	/**
	 * 获取字段注解信息
	 */
	public static List<Column> getColumns(Class<?> clazz) {
		List<Column> fields = Lists.newArrayList();
		List<Field> tempFields = new ArrayList<>();
		tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
		tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		for (Field field : tempFields) {
			// 单注解
			if (field.isAnnotationPresent(FieldName.class)) {
				FieldName attr = field.getAnnotation(FieldName.class);
				if (attr != null) {
					field.setAccessible(true);
					fields.add(Column.builder() //
							.columnName(attr.value()) //
							.columnValue(field.getName()) //
							.build());
				}
			}
		}
		return fields;
	}
}
