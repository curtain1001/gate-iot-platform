package net.pingfang.business.tcpserver;

import java.util.HashMap;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.device.LaneDeviceSupport;
import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.gate.protocol.codec.MessageType;
import net.pingfang.gate.protocol.codec.Packet;
import net.pingfang.gate.protocol.request.HeartBeatRequestPacket;
import net.pingfang.gate.protocol.response.HeartBeatResponsePacket;

@Slf4j
public class ServerHeartBeatRequestHandler extends ChannelInboundHandlerAdapter {

	private final IBtpLaneService laneService;

	public ServerHeartBeatRequestHandler(IBtpLaneService laneService) {
		this.laneService = laneService;
	}

	private static final HashMap<String, Channel> client = Maps.newHashMap();

	protected void put(String clientId, Channel channel) {
		client.put(clientId, channel);
	}

	public static Channel get(String clientId) {
		log.info("客户端连接-{}", clientId);
		return client.get(clientId);
	}

	protected void remove(String clientId) {
		log.info("客户端断开-{}", client);
		client.remove(clientId);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		LambdaQueryWrapper<BtpLane> queryWrapper = Wrappers.lambdaQuery();
		String ip = ServerUtils.getIp(ctx.channel());
		queryWrapper.eq(BtpLane::getIp, ip);
		List<BtpLane> btpLaneList = laneService.list(queryWrapper);
		if (!btpLaneList.isEmpty()) {
			BtpLane btpLane = btpLaneList.get(0);
			LaneDeviceSupport.addLane(btpLane.getLaneNo(), ctx.channel());
		}
		ctx.fireChannelActive();
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		LaneDeviceSupport.remove(ctx.channel());
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("心跳处理器收到报文：{}", JsonUtils.toJsonString(msg));
		Packet packet = (Packet) msg;
		try {
			if (packet.getCommand() != null && packet.getCommand() == MessageType.HEARTBEAT_REQUEST.value()) {
				HeartBeatRequestPacket requestPacket = (HeartBeatRequestPacket) packet;
				log.info("收到心跳报文：{}", JsonUtils.toJsonString(requestPacket));
				HeartBeatResponsePacket responsePacket = HeartBeatResponsePacket.builder()
						.messageId(requestPacket.getMessageId())//
						.success(true)//
						.message("心跳成功")//
						.build();
				ctx.writeAndFlush(responsePacket);
				log.info("发送心跳响应报文：{}", JsonUtils.toJsonString(responsePacket));
			}
		} catch (Exception e) {
			log.error("心跳处理器异常：", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
		ctx.fireChannelRead(msg);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println(ctx.channel().remoteAddress() + "关闭了连接...");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("netty异常：" + cause);
	}

}
