package net.pingfang.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 流程定义节点表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("btp_flow_runtime")
public class FlowDefinitionNode extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 定义代码
	 */
	private String flowId;

	/**
	 * 父节点代码
	 */
	private String parentCode;

	/**
	 * 节点代码
	 */
	private String nodeCode;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 条件脚本
	 */
	private String conditionScript;

	/**
	 * 优先级
	 */
	private Integer priority;

}
