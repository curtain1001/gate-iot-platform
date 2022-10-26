package net.pingfang.common.config;

import javax.annotation.Resource;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 16:49
 */
//常用的三个配置如下
//1---设置手动应答（acknowledge-mode: manual）
// 2---设置生产者消息发送的确认回调机制 (  #这个配置是保证提供者确保消息推送到交换机中，不管成不成功，都会回调
//    publisher-confirm-type: correlated
//    #保证交换机能把消息推送到队列中
//    publisher-returns: true
//     template:
//      #以下是rabbitmqTemplate配置
//      mandatory: true)
// 3---设置重试
@Configuration
@Slf4j
public class RabbitConfig {

	@Resource
	private ConnectionFactory rabbitConnectionFactory;

	@Resource
	private RabbitProperties properties;

	// 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的listener下的simple配置），如果想自定义来区分开 需要改变bean
	// 的名称
	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		CustomRabbitListenerContainerFactory containerFactory = new CustomRabbitListenerContainerFactory();
		containerFactory.setConnectionFactory(rabbitConnectionFactory);
		containerFactory.setErrorHandler((throwable) -> {
			log.error("MQ异常：{}", throwable.getLocalizedMessage());
		});
		// 并发消费者数量
		containerFactory.setConcurrentConsumers(1);
		containerFactory.setMaxConcurrentConsumers(20);
		// 预加载消息数量 -- QOS
		containerFactory.setPrefetchCount(1);
		// 应答模式（此处设置为手动）
		containerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		// 消息序列化方式
		containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
		// 设置通知调用链 （这里设置的是重试机制的调用链）
		containerFactory.setAdviceChain(RetryInterceptorBuilder.stateless() //
				.recoverer(new RejectAndDontRequeueRecoverer()) //
				.retryOperations(rabbitRetryTemplate()).build());
		return containerFactory;
	}

	static class CustomRabbitListenerContainerFactory extends SimpleRabbitListenerContainerFactory {

		@Override
		protected void initializeContainer(SimpleMessageListenerContainer instance, RabbitListenerEndpoint endpoint) {
			super.initializeContainer(instance, endpoint);
			instance.setAutoDeclare(true);
			instance.setMismatchedQueuesFatal(true);
		}

	}

	// 存在此名字的bean 自带的容器工厂会不加载（yml下rabbitmq下的template的配置），如果想自定义来区分开 需要改变bean 的名称
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(rabbitConnectionFactory);
		// 默认是用jdk序列化
		// 数据转换为json存入消息队列，方便可视化界面查看消息数据
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		// 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
		rabbitTemplate.setMandatory(true);
		// 此处设置重试template后，会再生产者发送消息的时候，调用该template中的调用链
		rabbitTemplate.setRetryTemplate(rabbitRetryTemplate());
		// CorrelationData correlationData, boolean b, String s
		rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {
			System.out.println("ConfirmCallback     " + "相关数据：" + correlationData);
			System.out.println("ConfirmCallback     " + "确认情况：" + b);
			System.out.println("ConfirmCallback     " + "原因：" + s);
		});
		// Message message, int i, String s, String s1, String s2
		rabbitTemplate.setReturnsCallback((message) -> {
			System.out.println("ReturnCallback：     " + "消息：" + message.getMessage());
			System.out.println("ReturnCallback：     " + "回应码：" + message.getReplyCode());
			System.out.println("ReturnCallback：     " + "回应消息：" + message.getReplyText());
			System.out.println("ReturnCallback：     " + "交换机：" + message.getExchange());
			System.out.println("ReturnCallback：     " + "路由键：" + message.getRoutingKey());
		});

		return rabbitTemplate;
	}

	// 重试的Template
	@Bean
	public RetryTemplate rabbitRetryTemplate() {
		RetryTemplate retryTemplate = new RetryTemplate();
		// 设置监听 调用重试处理过程
		retryTemplate.registerListener(new RetryListener() {
			@Override
			public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
				// 执行之前调用 （返回false时会终止执行）
				return true;
			}

			@Override
			public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback,
					Throwable throwable) {
				// 重试结束的时候调用 （最后一次重试 ）
				System.out.println("---------------最后一次调用");
				return;
			}

			@Override
			public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback,
					Throwable throwable) {
				// 异常 都会调用
				System.err.println("-----第{}次调用" + retryContext.getRetryCount());
			}
		});
		retryTemplate.setBackOffPolicy(backOffPolicyByProperties());
		retryTemplate.setRetryPolicy(retryPolicyByProperties());
		return retryTemplate;
	}

	@Bean
	public ExponentialBackOffPolicy backOffPolicyByProperties() {
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		long maxInterval = properties.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
		long initialInterval = properties.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
		double multiplier = properties.getListener().getSimple().getRetry().getMultiplier();
		// 重试间隔
		backOffPolicy.setInitialInterval(initialInterval * 1000);
		// 重试最大间隔
		backOffPolicy.setMaxInterval(maxInterval * 1000);
		// 重试间隔乘法策略
		backOffPolicy.setMultiplier(multiplier);
		return backOffPolicy;
	}

	@Bean
	public SimpleRetryPolicy retryPolicyByProperties() {
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
		int maxAttempts = properties.getListener().getSimple().getRetry().getMaxAttempts();
		retryPolicy.setMaxAttempts(maxAttempts);
		return retryPolicy;
	}
}
