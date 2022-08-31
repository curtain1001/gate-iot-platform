package net.pingfang.business.domain;

import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 23:09
 */
@EqualsAndHashCode(callSuper = true)
@TableName("btp_instruction")
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BtpInstruction extends BaseEntity {
	private static final long serialVersionUID = -4442977386547872891L;
	/**
	 * 指令主键
	 */
	@TableId(type = IdType.AUTO)
	private Long instrId;

	/**
	 * 设备产品类型
	 */
	private String product;
	/**
	 * 命令名称
	 */
	private String commandName;

	/**
	 * 命令代码（为系统匹配键值：唯一）
	 */
	private String commandValue;

	/**
	 * 指令运行类型 （上行：下行）
	 */
	private String type;
	/**
	 * 报文
	 */
	private Object content;
	/**
	 * 报文格式类型（Json；）
	 */
	private String format;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 指令配置信息
	 */
	private Map<String, Object> configuration;
}
