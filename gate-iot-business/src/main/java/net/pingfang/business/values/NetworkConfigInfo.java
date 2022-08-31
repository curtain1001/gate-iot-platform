package net.pingfang.business.values;

import org.springframework.util.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-24 11:03
 */
public class NetworkConfigInfo {

	/**
	 * 网络组件ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 详情
	 *
	 * @return
	 */
	public String getDetail() {
		if (StringUtils.hasText(address)) {
			return name + "(" + address + ")";
		}
		return name;
	}
}
