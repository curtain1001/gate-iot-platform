package net.pingfang.common.pool;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 对象池的基础实现 这里用阻塞队列来实现
 * </p>
 *
 * @author 王超
 * @since 2022-10-11 18:26
 */
public class BaseCommonPool<T> implements ICommonPool<T> {
	private static final Logger logger = LoggerFactory.getLogger(BaseCommonPool.class);
	private LinkedBlockingQueue<T> pool;

	private final PoolConfig poolConfig;
	private final PoolFactory<T> poolFactory;

	public BaseCommonPool(PoolConfig poolConfig, PoolFactory<T> poolFactory) {
		this.poolConfig = poolConfig;
		this.poolFactory = poolFactory;
		System.setProperty("jna.encoding", "GB2312");
		initCommonPool();
	}

	private void initCommonPool() {
		// 代表池的大小
		pool = new LinkedBlockingQueue<T>(poolConfig.getMaxTotal());
		while (poolConfig.getMinTotal() > pool.size()) {
			T obj = poolFactory.makeObject();
			pool.offer(obj);
		}
	}

	/**
	 * 租借对象
	 */
	public T borrowObject() {
		// 如果队列中对象为空，poll方法返回null，非阻塞（take方法阻塞）
		T obj = pool.poll();
		if (obj != null) {
			return obj;
		}
		return poolFactory.makeObject();
	}

	/**
	 * 返回对象池
	 */
	public void returnObject(T object) {
		// offer方法非阻塞，如果队列满了，直接抛弃，返回false，与put方法的区别自行百度
		if (!pool.offer(object)) {
			poolFactory.destroyObject(object);
		}
	}

	public void destroy() {
		pool.stream().peek(poolFactory::destroyObject);
		pool.clear();
	}
}
