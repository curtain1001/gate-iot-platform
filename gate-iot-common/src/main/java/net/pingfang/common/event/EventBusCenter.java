package net.pingfang.common.event;

import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import net.pingfang.common.event.EventBusListener.Type;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-16 14:22
 */
@Component
public class EventBusCenter implements BeanPostProcessor {

	// 管理同步事件
	private EventBus syncEventBus = new EventBus();

	// 管理异步事件
	private AsyncEventBus asyncEventBus = new AsyncEventBus(Executors.newCachedThreadPool());

	public void postSync(Object event) {
		syncEventBus.post(event);
	}

	public void postAsync(Object event) {
		asyncEventBus.post(event);
	}

//	@PostConstruct
//	public void init() {
//		// 获取所有带有 @EventBusListener 的 bean，将他们注册为监听者
//		Collection<Object> listeners = SpringUtils.getBean(Product.class);
//		for (Object listener : listeners) {
//			asyncEventBus.register(listener);
//			syncEventBus.register(listener);
//		}
//	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		EventBusListener listener = AnnotationUtils.getAnnotation(bean.getClass(), EventBusListener.class);
		if (listener != null) {
			if (Type.SYNC == listener.type()) {
				syncEventBus.register(bean);
			} else if (Type.ASYNC == listener.type()) {
				asyncEventBus.register(bean);
			}
		}

		return bean;
	}
}
