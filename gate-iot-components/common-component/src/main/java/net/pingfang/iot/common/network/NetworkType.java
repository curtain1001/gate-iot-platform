package net.pingfang.iot.common.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.Lists;

import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;

/**
 * 网络组件类型，通常使用枚举实现
 *
 * @author zhouhao
 * @since 1.0
 */
public interface NetworkType {
	/**
	 * @return 类型唯一标识
	 */
	String getId();

	default List<CustomizedSettingData> getBasicForm() {
		return Lists.newArrayList();
	}

	/**
	 * @return 类型名称
	 */
	default String getName() {
		return getId();
	}

	default String generateId() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
		return getName() + "::" + dateFormat.format(new Date());
	}

	/**
	 * 使用指定的ID创建一个NetworkType
	 *
	 * @param id ID
	 * @return NetworkType
	 */
	static NetworkType of(String id) {
		return () -> id;
	}

	/**
	 * 获取所有支持的网络组件类型
	 *
	 * @return 所有支持的网络组件类型
	 */
	static List<NetworkType> getAll() {
		return NetworkTypes.get();
	}

	/**
	 * 根据网络组件类型ID获取类型对象
	 *
	 * @param id ID
	 * @return Optional
	 */
	static Optional<NetworkType> lookup(String id) {
		return NetworkTypes.lookup(id);
	}

}
