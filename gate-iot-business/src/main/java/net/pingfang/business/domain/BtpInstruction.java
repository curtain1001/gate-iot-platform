package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 23:09
 */
@TableName("btp_instruction")
public class BtpInstruction extends BaseEntity {
	/**
	 * 指令主键
	 */
	@TableId(type = IdType.AUTO)
	private Long instrId;
	/**
	 * 设备代码
	 */
	private String deviceId;
	/**
	 * 设备产品类型
	 */
	private String product;
	/**
	 * 命令
	 */
	private String command;
	/**
	 * 报文
	 */
	private Object content;
}
