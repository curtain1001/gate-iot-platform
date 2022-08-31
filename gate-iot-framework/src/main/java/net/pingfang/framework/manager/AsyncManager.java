package net.pingfang.framework.manager;

import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.pingfang.common.utils.Threads;
import net.pingfang.common.utils.spring.SpringUtils;

/**
 * 异步任务管理器
 *
 * @author ruoyi
 */
public class AsyncManager {
	private static AsyncManager me = new AsyncManager();
	/**
	 * 操作延迟10毫秒
	 */
	private final int OPERATE_DELAY_TIME = 10;
	/**
	 * 异步操作任务调度线程池
	 */
	private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

	/**
	 * 单例模式
	 */
	private AsyncManager() {
	}

	public static AsyncManager me() {
		return me;
	}

	/**
	 * 执行任务
	 *
	 * @param task 任务
	 */
	public void execute(TimerTask task) {
		executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}

	public void execute(Runnable runnable) {
		executor.execute(runnable);
	}

	public Future<?> execute(Callable<?> callable) {
		return executor.submit(callable);
	}

	public void execute(FutureTask<?> task) {
		executor.submit(task);
	}

	/**
	 * 停止任务线程池
	 */
	public void shutdown() {
		Threads.shutdownAndAwaitTermination(executor);
	}
}
