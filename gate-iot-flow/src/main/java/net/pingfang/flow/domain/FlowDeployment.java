package net.pingfang.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
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
@TableName(value = "btp_flow_deployment", autoResultMap = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FlowDeployment extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long deployId;

	/**
	 * 流程定义Id
	 */
	private Long flowId;

	/**
	 * 车道Id
	 */
	private Long laneId;

	/**
	 * 流程信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private JsonNode content;

	/**
	 * 版本号
	 */
	private int version;

	@TableLogic
	private Integer deleted;

}
