package net.pingfang.business.tcpserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.repository.CloudGateRepository;
import net.pingfang.business.values.ArriveLeave;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.gate.protocol.codec.MessageType;
import net.pingfang.gate.protocol.codec.Packet;
import net.pingfang.gate.protocol.request.LaneFlowRequestPacket;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 10:36
 */
@Slf4j
@AllArgsConstructor
public class ServerLaneFlowHandler extends ChannelInboundHandlerAdapter {

	final private CloudGateRepository repository;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("车道流程处理器收到报文：" + JsonUtils.toJsonString(msg));
		try {
			Packet packet = (Packet) msg;
			switch (MessageType.valueOf(packet.getCommand())) {
			// 车道流程请求
			case LANE_FLOW_REQUEST:
				LaneFlowRequestPacket requestPacket = (LaneFlowRequestPacket) packet;
				switch (requestPacket.getLaneFlowNode()) {
				// 流程开始
				case start: {
					ArriveLeave arriveLeave = ArriveLeave.builder() //
							.areaNo(Long.parseLong(packet.getAreaNo()))//
							.laneNo(packet.getLaneNo())//
							.dateTime(packet.getDate()) //
							.inExitFlag("I")//
							.build();
					AjaxResult result = repository.arriveOrLeave(arriveLeave);
				}
				// 作业报文提交
				case submit: {

				}
				// 网页提交
				case websubmit: {

				}
				// 流程结束
				case end: {

				}

				}

			}
		} catch (Exception e) {
			log.error("车道流程处理器处理器异常：", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
		ctx.fireChannelRead(msg);
	}
}
