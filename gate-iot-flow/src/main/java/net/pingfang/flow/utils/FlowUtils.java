package net.pingfang.flow.utils;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.flow.domain.FlowEdge;
import net.pingfang.flow.domain.FlowNode;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 17:16
 */
public class FlowUtils {
	public static FlowNode nodeConvert(JsonNode jsonNode) {
		JsonNode properties = jsonNode.get("properties");
		FlowNode flowNode = FlowNode.builder() //
				// .deployId()
				.nodeId(jsonNode.get("id").asText())//
				.type(jsonNode.get("type").asText())//
				.content(jsonNode)//
				.build();
		flowNode = flowNode.setProperties(properties);
		return flowNode;
	}

	public static FlowEdge edgeConvert(JsonNode edge) {
		JsonNode properties = edge.get("properties");
		FlowEdge flowEdge = FlowEdge.builder() //
				// .deployId()
				.edgeId(edge.get("id").asText())//
				.type(edge.get("type").asText())//
				.sourceNodeId(edge.get("sourceNodeId").asText())//
				.targetNodeId(edge.get("targetNodeId").asText())//
				.content(edge)//
				.build();
		flowEdge = flowEdge.setProperties(properties);
		return flowEdge;
	}
}
