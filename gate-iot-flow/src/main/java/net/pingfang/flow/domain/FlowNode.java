package net.pingfang.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.flow.enums.NodeType;
import net.pingfang.flow.values.NodeProperties;

/**
 * <p>
 * 流程定义节点表
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Data
@Slf4j
@Builder(toBuilder = true)
@TableName(value = "btp_flow_node", autoResultMap = true)
public class FlowNode {

	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.INPUT)
	private String nodeId;
	/**
	 * 节点名称 text=>value
	 */
	private String nodeName;

	/**
	 * 部署流程id
	 */
	private Long deployId;

	/**
	 * 节点类型
	 */
	private NodeType type;
	/**
	 * 节点配置信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private NodeProperties properties;

	/**
	 * 报文
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private JsonNode content;
	/**
	 * 序号
	 */
	private int seq;

}
