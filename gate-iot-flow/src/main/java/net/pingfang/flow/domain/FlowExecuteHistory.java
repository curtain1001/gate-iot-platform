package net.pingfang.flow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.instruction.Instruction;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 10:57
 */
@Data
@Builder
@TableName("btp_flow_execute_history")
public class FlowExecuteHistory {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 流程实例id
	 */
	private Long instanceId;

	/**
	 * 节点id
	 */
	private Integer nodeId;
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
	 * 执行时间
	 */
	private Date actionTime;
	/**
	 * 执行结果
	 */
	private Object result;
}
