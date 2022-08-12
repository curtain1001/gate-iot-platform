package net.pingfang.flow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

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
@Builder(toBuilder = true)
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
	 * 执行结果
	 */
	private Object result;

	/**
	 * 创建时间
	 *
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
