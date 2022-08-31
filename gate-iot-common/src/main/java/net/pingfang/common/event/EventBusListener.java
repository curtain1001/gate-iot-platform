package net.pingfang.common.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 事件监听器注解
 * </p>
 *
 * @author 王超
 * @since 2022-08-16 14:23
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBusListener {
	Type type() default Type.SYNC;

	enum Type {
		SYNC, // 同步
		ASYNC // 异步
	}
}
