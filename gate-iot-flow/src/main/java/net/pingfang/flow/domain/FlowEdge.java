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
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.flow.values.EdgeProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 14:29
 */
@Data
@Slf4j
@Builder(toBuilder = true)
@TableName(value = "btp_flow_edge", autoResultMap = true)
public class FlowEdge {

	private static final long serialVersionUID = 6606708399600610831L;
	/**
	 * 连线id
	 */
	@TableId(type = IdType.INPUT)
	private String edgeId;
	/**
	 * 部署Id
	 */
	private Long deployId;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 源节点id
	 */
	private String sourceNodeId;
	/**
	 * 目标节点id
	 */
	private String targetNodeId;
	/**
	 * 完整报文
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private JsonNode content;
	/**
	 * 节点配置信息
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private EdgeProperties properties;

	/**
	 * 序号
	 */
	private int seq;

	public FlowEdge setProperties(JsonNode jsonNode) {
		JsonNode node = jsonNode.get("properties");
		this.properties = JsonUtils.nodeToObject(node, EdgeProperties.class);
		return this;
	}
}
