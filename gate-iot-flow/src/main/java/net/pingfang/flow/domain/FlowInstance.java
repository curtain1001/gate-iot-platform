package net.pingfang.flow.domain;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 流程实例表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_instance")
public class FlowInstance extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 实例代码
	 */
	private String instanceCode;

	/**
	 * 定义代码
	 */
	private String definitionCode;

	/**
	 * 申请人用户名
	 */
	private String username;

	/**
	 * 申请人姓名
	 */
	private String fullname;

	/**
	 * 模块id
	 */
	private String moduleId;

	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 申请标题
	 */
	private String title;

	/**
	 * 状态：0进行中，1已完成，2已终止等
	 */
	private Integer status;

	/**
	 * 申请时间
	 */
	private LocalDateTime applyTime;

}
