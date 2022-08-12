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
import net.pingfang.flow.enums.InstanceStatus;

/**
 * <p>
 * 流程实例表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@TableName("btp_flow_process_instance")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class FlowProcessInstance {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long instanceId;

	/**
	 * 部署中的流程主键
	 */
	private Long deployId;

	/**
	 * 车道id
	 */
	private Long laneId;

	/**
	 * 状态：0进行中，1已完成，2已终止等
	 */
	private InstanceStatus status;

	/**
	 * 流程开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	/**
	 * 流程结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

}
