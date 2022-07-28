package net.pingfang.flow.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 任务实例
 * </p>
 *
 * @author 王超
 * @description
 * @date 2022-07-28 16:01
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskInstance extends BaseEntity {

	private static final long serialVersionUID = 423415935151581331L;

	private Long taskId;

	/**
	 * 任务编号
	 */
	private String taskCode;
	/**
	 * 实例代码
	 */
	private String instanceCode;

	/**
	 * 定义代码
	 */
	private String definitionCode;

	/**
	 * 车道id
	 */
	private Long laneId;

	/**
	 * 状态：0进行中，1已完成，2已终止等
	 */
	private Integer status;

	/**
	 * 状态：0待处理，1过闸，2终止过闸
	 */
	private Integer actionStatus;
}
