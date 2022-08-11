package net.pingfang.flow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pingfang.flow.enums.ProcessStatus;

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
@TableName(value = "btp_flow_execute_history", autoResultMap = true)
@AllArgsConstructor
@NoArgsConstructor
public class FlowExecuteHistory {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long historyId;
	/**
	 * 流程实例id
	 */
	private Long instanceId;

	/**
	 * 节点id
	 */
	private String nodeId;
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * 执行指令
	 */
	private String instruction;
	/**
	 * 状态：0待处理，1成功，2失败
	 */
	private ProcessStatus status;
	/**
	 * 执行时间
	 */
	private Date actionTime;
	/**
	 * 执行结果
	 */
	private Object result;
}
