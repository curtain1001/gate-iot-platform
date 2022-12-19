package net.pingfang.common.customizedsetting;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-23 10:57
 */
public interface Customized {
	/**
	 * 标题
	 *
	 * @return
	 */
	String getLabel();

	/**
	 * value
	 *
	 * @return
	 */
	String getValue();

	/**
	 * 类型
	 *
	 * @return
	 */
	String getType();

	/**
	 * 可选值
	 *
	 * @return
	 */
	String getOptions();

	/**
	 * 自定义大类
	 *
	 * @return
	 */
	String getCustomizeType();

	/**
	 * 默认值
	 *
	 * @return
	 */
	Object getDefaults();

}
