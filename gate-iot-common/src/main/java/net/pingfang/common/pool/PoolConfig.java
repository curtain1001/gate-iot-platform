package net.pingfang.common.pool;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-11 18:25
 */
public class PoolConfig {

	// 池中最大对象
	private int maxTotal = 8;
	// 初始化对象数
	private int minTotal = 0;

//	public PoolConfig(int maxTotal, int minTotal) {
//		this.maxTotal = maxTotal;
//		this.minTotal = minTotal;
//	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMinTotal() {
		return minTotal;
	}

	public void setMinTotal(int minTotal) {
		this.minTotal = minTotal;
	}
}
