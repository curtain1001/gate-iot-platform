package net.pingfang.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 流程实例变量
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_variable")
public class FlowVariable extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 实例代码
	 */
	private String instanceCode;

	/**
	 * 变量键
	 */
	private String key;

	/**
	 * 变量值
	 */
	private String value;

	/**
	 * 变量类型
	 */
	private String type;

}
