package net.pingfang.common.pool;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-11 18:24
 */
public interface ICommonPool<T> {
	/**
	 * 租借对象
	 */
	T borrowObject() throws Exception;

	/**
	 * 返回对象池
	 */
	void returnObject(T object) throws Exception;
}
