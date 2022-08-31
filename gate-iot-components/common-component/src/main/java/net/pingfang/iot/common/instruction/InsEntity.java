package net.pingfang.iot.common.instruction;

import lombok.Builder;
import lombok.Data;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 11:42
 */
@Data
@Builder(toBuilder = true)
public class InsEntity {
	/**
	 * 设备id
	 */
	private String deviceId;
	/**
	 * 命令名称
	 */
	private String name;

	/**
	 * 指令代码
	 *
	 * @return
	 */
	private String value;
	/**
	 * 产品类型
	 */
	private String product;
	/**
	 * 报文内容
	 */
	private Object content;

	/**
	 * 指令运行类型 （上行：下行）
	 */
	private String type;

	/**
	 * 报文格式类型（Json；）
	 */
	private FormatType format;

}
