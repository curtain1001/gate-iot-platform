package net.pingfang.common.pool;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-11 18:24
 */
public interface PoolFactory<T> {

	/**
	 * 创建对象
	 *
	 * @return T
	 */
	T makeObject();

	/**
	 * 销毁对象
	 */
	void destroyObject(T object);
}
