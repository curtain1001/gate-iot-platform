package net.pingfang.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 * 流程定义表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("btp_flow_procdef")
public class FlowDefinition extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 定义代码
	 */
	private String flowId;

	/**
	 * 流程定义名称
	 */
	private String laneId;

	/**
	 * 版本号
	 */
	private String version;

}
