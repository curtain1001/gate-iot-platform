package net.pingfang.business.plugin;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 17:40
 */
@Slf4j
public class ClassLoaderUtil {
	/**
	 * 其中在创建 URLClassLoader 时，指定当前系统的 ClassLoader 为父类加载器
	 * ClassLoader.getSystemClassLoader() 这步比较关键，用于打通主程序与插件之间的 ClassLoader ，解决把插件注册进
	 * IOC 时的各种 ClassNotFoundException 问题。
	 *
	 * @param url
	 * @return
	 */
	public static ClassLoader getClassLoader(String url) {
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			URLClassLoader classLoader = new URLClassLoader(new URL[] {}, ClassLoader.getSystemClassLoader());
			method.invoke(classLoader, new URL(url));
			return classLoader;
		} catch (Exception e) {
			log.error("getClassLoader-error", e);
			return null;
		}
	}
}
