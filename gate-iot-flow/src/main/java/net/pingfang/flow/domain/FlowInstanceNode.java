package net.pingfang.flow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.common.core.domain.BaseEntity;
import net.pingfang.iot.common.instruction.Instruction;

/**
 * <p>
 * 流程实例节点表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_instance_node")
public class FlowInstanceNode extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 节点id
	 */
	private Integer nodeId;

	/**
	 * 父节点id
	 */
	private Integer parentId;

	/**
	 * 实例代码
	 */
	private String instanceCode;

	/**
	 * 任务编号
	 */
	private String taskCode;

	/**
	 * 节点代码
	 */
	private String nodeCode;

	/**
	 * 节点名称
	 */
	private String nodeName;

	/**
	 * 执行指令
	 */
	private Instruction instruction;

	/**
	 * 状态：0待处理，1成功，2失败
	 */
	private Integer actionStatus;

	/**
	 * 批阅时间
	 */
	private Date actionTime;

}
