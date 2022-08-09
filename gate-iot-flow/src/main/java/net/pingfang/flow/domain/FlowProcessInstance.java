package net.pingfang.flow.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;
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
@EqualsAndHashCode(callSuper = true)
@TableName("flow_process_instance")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FlowProcessInstance extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long instanceId;

	/**
	 * 部署中的流程主键
	 */
	private Long deployFlowId;

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
	private Date startTime;

	/**
	 * 流程结束时间
	 */
	private Date endTime;

}
