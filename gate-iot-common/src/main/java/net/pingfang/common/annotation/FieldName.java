package net.pingfang.common.annotation;

/**
 * <p>
 * 用于构建对象字段与中文注释键值对
 * </p>
 *
 * @author 王超
 * @since 2022-09-22 11:47
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {
	/**
	 * 字段中文title
	 *
	 */
	public String value() default "";
}
