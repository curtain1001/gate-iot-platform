package net.pingfang.common.utils.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.StringUtils;

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author ruoyi
 */
@Component
@Slf4j
public final class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
	/** Spring应用上下文环境 */
	private static ConfigurableListableBeanFactory beanFactory;

	private static ApplicationContext applicationContext;

	public static ApplicationContext getContext() {
		return applicationContext;
	}

	/**
	 * 获取对象
	 *
	 * @param name
	 * @return Object 一个以所给名字注册的bean的实例
	 * @throws org.springframework.beans.BeansException
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T) beanFactory.getBean(name);
	}

	/**
	 * 获取类型为requiredType的对象
	 *
	 * @param clz
	 * @return
	 * @throws org.springframework.beans.BeansException
	 *
	 */
	public static <T> T getBean(Class<T> clz) throws BeansException {
		T result = (T) beanFactory.getBean(clz);
		return result;
	}

	/**
	 * 获取标记注解的对象
	 *
	 * @param annotation
	 * @return
	 * @throws org.springframework.beans.BeansException
	 *
	 */
	public static Collection<Object> getBeansWithAnnotation(Class<? extends Annotation> annotation)
			throws BeansException {
		Map<String, Object> beanMap = beanFactory.getBeansWithAnnotation(annotation);
		return beanMap.values();
	}

	/**
	 * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
	 *
	 * @param name
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		return beanFactory.containsBean(name);
	}

	/**
	 * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。
	 * 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
	 *
	 * @param name
	 * @return boolean
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 *
	 */
	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.isSingleton(name);
	}

	/**
	 * 方法描述 判断class对象是否带有spring的注解 存放實現
	 *
	 * @param cla jar中的每一个class
	 * @return true 是spring bean false 不是spring bean
	 * @method isSpringBeanClass
	 */
	public static boolean isSpringBeanClass(Class<?> cla) {

		if (cla == null) {
			return false;
		}
		// 是否是接口
		if (cla.isInterface()) {
			return false;
		}
		// 是否是抽象类
		if (Modifier.isAbstract(cla.getModifiers())) {
			return false;
		}
		try {
			if (cla.getAnnotation(Component.class) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error("出现异常：{}", e.getMessage());
		}

		try {
			if (cla.getAnnotation(Repository.class) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error("出现异常：{}", e.getMessage());
		}

		try {
			if (cla.getAnnotation(Service.class) != null) {
				return true;
			}
		} catch (Exception e) {
			log.error("出现异常：{}", e.getMessage());
		}
		return false;
	}

	/**
	 * @param name
	 * @return Class 注册对象的类型
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 *
	 */
	public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.getType(name);
	}

	/**
	 * 如果给定的bean名字在bean定义中有别名，则返回这些别名
	 *
	 * @param name
	 * @return
	 * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
	 *
	 */
	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
		return beanFactory.getAliases(name);
	}

	/**
	 * 获取aop代理对象
	 *
	 * @param invoker
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAopProxy(T invoker) {
		return (T) AopContext.currentProxy();
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 */
	public static String[] getActiveProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * 获取当前的环境配置，当有多个环境配置时，只获取第一个
	 *
	 * @return 当前的环境配置
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return StringUtils.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 获取配置文件中的值
	 *
	 * @param key 配置文件的key
	 * @return 当前的配置文件的值
	 *
	 */
	public static String getRequiredProperty(String key) {
		return applicationContext.getEnvironment().getRequiredProperty(key);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtils.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.applicationContext = applicationContext;
	}
}
