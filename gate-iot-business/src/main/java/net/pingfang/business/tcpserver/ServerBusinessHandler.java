package net.pingfang.business.tcpserver;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.device.LaneDeviceSupport;
import net.pingfang.business.domain.BtpDevice;
import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.StringUtils;
import net.pingfang.gate.protocol.codec.MessageType;
import net.pingfang.gate.protocol.codec.Packet;
import net.pingfang.gate.protocol.request.AllDeviceInfoRequestPacket;
import net.pingfang.gate.protocol.request.DeviceInfoRequestPacket;
import net.pingfang.gate.protocol.request.InitClientLaneRequestPacket;
import net.pingfang.gate.protocol.request.LaneInfoRequestPacket;
import net.pingfang.gate.protocol.response.AllDeviceInfoResponsePacket;
import net.pingfang.gate.protocol.response.DeviceInfoResponsePacket;
import net.pingfang.gate.protocol.response.InitClientLaneResponsePacket;
import net.pingfang.gate.protocol.response.LaneInfoResponsePacket;
import net.pingfang.gate.protocol.values.DeviceInfo;
import net.pingfang.gate.protocol.values.SystemConfig;

/**
 * 业务处理器
 */
@Slf4j
public class ServerBusinessHandler extends ChannelInboundHandlerAdapter {

	private IBtpLaneService laneService;

	private IBtpDeviceService deviceService;

	public ServerBusinessHandler(IBtpLaneService laneService, IBtpDeviceService deviceService) {
		this.laneService = laneService;
		this.deviceService = deviceService;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("业务处理器收到报文：" + JsonUtils.toJsonString(msg));
		try {
			Packet packet = (Packet) msg;
			switch (MessageType.valueOf(packet.getCommand())) {
			case INIT_CLIENT_LANE_REQUEST: {
				InitClientLaneResponsePacket responsePacket;
				InitClientLaneRequestPacket initRequestPacket = (InitClientLaneRequestPacket) packet;
				String laneNo = initRequestPacket.getLaneNo();
				if (StringUtils.isEmpty(laneNo)) {
					laneNo = LaneDeviceSupport.getLaneNo(ctx.channel().id().asLongText());
				}
				BtpLane b = laneService.load(laneNo);
				if (b == null) {
					responsePacket = InitClientLaneResponsePacket.builder()//
							.messageId(packet.getMessageId())//
							.success(false)//
							.build();
					ctx.writeAndFlush(responsePacket);
					break;
				}
				List<BtpDevice> btpDevices = deviceService.selectByLaneId(b.getLaneId());
				responsePacket = InitClientLaneResponsePacket.builder()//
						.messageId(packet.getMessageId())//
						.success(true)//
						.laneName(b.getLaneName())//
						.laneNo(b.getLaneNo())//
						.deviceInfoList(btpDevices.stream().map(x -> DeviceInfo.builder() //
								.deviceNo(x.getDeviceId())//
								.deviceName(x.getDeviceName())//
								.deviceProduct(x.getProduct())//
								.enabled(x.isEnabled())//
								.configuration(x.getConfiguration())//
								.build()).collect(Collectors.toList()))//
						.config(SystemConfig.builder()//
								.build())
						.build();
				ctx.writeAndFlush(responsePacket);
				break;
			}

			// 车道信息响应
			case LANE_INFO_REQUEST: {
				LaneInfoRequestPacket laneInfoRequestPacket = (LaneInfoRequestPacket) packet;
				String laneNo = laneInfoRequestPacket.getLaneNo();
				BtpLane laneInfo;
				if (StringUtils.isEmpty(laneNo)) {
					laneNo = LaneDeviceSupport.getLaneNo(ctx.channel().id().asLongText());
				}
				LambdaQueryWrapper<BtpLane> queryWrapper = Wrappers.lambdaQuery();
				queryWrapper.eq(BtpLane::getLaneNo, laneNo);
				laneInfo = laneService.getOne(queryWrapper);
				LaneInfoResponsePacket laneInfoResponsePacket;
				if (laneInfo != null) {
					laneInfoResponsePacket = LaneInfoResponsePacket.builder() //
							.messageId(laneInfoRequestPacket.getMessageId())//
							.success(true)//
							.laneId(laneInfo.getLaneId())//
							.laneNo(laneInfo.getLaneNo())//
							.laneName(laneInfo.getLaneName())//
							.type(laneInfo.getType())//
							.customsLaneNo(laneInfo.getCustomsLaneNo())//
							.build();
				} else {
					laneInfoResponsePacket = LaneInfoResponsePacket.builder() //
							.messageId(laneInfoRequestPacket.getMessageId())//
							.success(false)//
							.message("不存在该车道") //
							.build();
				}
				ctx.writeAndFlush(laneInfoResponsePacket);
				log.info("发送车道信息响应报文：{}", JsonUtils.toJsonString(laneInfoResponsePacket));
				break;
			}
			// 设备信息响应
			case DEVICE_INFO_REQUEST: {
				DeviceInfoRequestPacket deviceInfoRequestPacket = (DeviceInfoRequestPacket) packet;
				DeviceInfoResponsePacket deviceInfoResponsePacket = DeviceInfoResponsePacket.builder()
						.messageId(deviceInfoRequestPacket.getMessageId())//
						.success(true)//
						.build();
				ctx.writeAndFlush(deviceInfoResponsePacket);
				log.info("发送设备信息响应报文：{}", JsonUtils.toJsonString(deviceInfoResponsePacket));
				break;
			}
			// 所有设备信息响应
			case ALL_DEVICE_INFO_REQUEST: {
				AllDeviceInfoRequestPacket requestPacket = (AllDeviceInfoRequestPacket) packet;
				String laneNo = requestPacket.getLaneNo();
				if (StringUtils.isEmpty(laneNo)) {
					laneNo = LaneDeviceSupport.getLaneNo(ctx.channel().id().asLongText());
				}
				BtpLane btpLane = laneService.load(laneNo);
				if (btpLane == null) {
					AllDeviceInfoResponsePacket deviceInfoResponsePacket = AllDeviceInfoResponsePacket.builder()
							.messageId(requestPacket.getMessageId())//
							.success(false)//
							.message("车道不存在")//
							.build();
					ctx.writeAndFlush(deviceInfoResponsePacket);
					break;
				}
				LambdaQueryWrapper<BtpDevice> queryWrapper = Wrappers.lambdaQuery();
				queryWrapper.eq(BtpDevice::getLaneId, btpLane.getLaneId());
				queryWrapper.eq(
						com.baomidou.mybatisplus.core.toolkit.StringUtils.checkValNotNull(requestPacket.getProduct()),
						BtpDevice::getProduct, requestPacket.getProduct());
				List<BtpDevice> deviceList = deviceService.list(queryWrapper);
				AllDeviceInfoResponsePacket deviceInfoResponsePacket = AllDeviceInfoResponsePacket.builder()
						.messageId(requestPacket.getMessageId())//
						.success(true)//
						.propertiesList(deviceList.stream()
								.map(info -> net.pingfang.gate.protocol.values.DeviceInfo.builder()
										.deviceNo(info.getDeviceId())//
										.deviceName(info.getDeviceName())//
										.deviceProduct(info.getProduct())//
										.enabled(info.isEnabled())//
										.configuration(info.getConfiguration())//
										.build())
								.collect(Collectors.toList()))//
						.build();
				ctx.writeAndFlush(deviceInfoResponsePacket);
				log.info("发送设备信息响应报文：{}", JsonUtils.toJsonString(deviceInfoResponsePacket));
				break;
			}
			}
		} catch (Exception e) {
			log.error("业务处理器异常：", e);
		} finally {
			ReferenceCountUtil.release(msg);
		}
		ctx.fireChannelRead(msg);
	}

}
