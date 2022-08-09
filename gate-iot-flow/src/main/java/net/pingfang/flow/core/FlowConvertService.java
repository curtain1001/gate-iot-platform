package net.pingfang.flow.core;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.flow.service.IFlowEdgeService;
import net.pingfang.flow.service.IFlowNodeService;

/**
 * <p>
 * 流程转换服务
 * </p>
 *
 * @author 王超
 * @since 2022-08-05 15:02
 */
public class FlowConvertService {
	@Resource
	public IFlowNodeService nodeService;
	@Resource
	public IFlowEdgeService edgeService;

	public void nodeConvert(JsonNode jsonNode) {

	}

}
