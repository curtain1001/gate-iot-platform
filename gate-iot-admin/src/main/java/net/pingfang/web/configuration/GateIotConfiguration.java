package net.pingfang.web.configuration;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.pingfang.business.service.IBtpDeviceService;
import net.pingfang.business.service.IBtpLaneService;
import net.pingfang.business.tcpserver.ServerHeartBeatRequestHandler;
import net.pingfang.common.tcp.server.TcpServer;
import net.pingfang.gate.protocol.codec.PacketDecoder;
import net.pingfang.gate.protocol.codec.PacketEncoder;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 10:51
 */
@Configuration
public class GateIotConfiguration {
	@Value("${gate-server.port}")
	private int port;
	@Resource
	private IBtpLaneService btpLaneService;
	@Resource
	private IBtpDeviceService deviceService;

	@Bean
	public TcpServer connect() {
		TcpServer tcpServer = new TcpServer(port);
		ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LoggingHandler(LogLevel.INFO));

				// // 剥离接受到的消息长度，获取实际的消息数据
				pipeline.addLast("lengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(65535, 3, 4, 0, 0));
				// // 给发送的消息添加消息长度
				// pipeline.addLast("lengthFieldPrepender", new LengthFieldPrepender(2));

				// 反序列化，将字节数组转换为消息
				pipeline.addLast(new PacketEncoder());
				// 序列化，将消息转化为字节数组
				pipeline.addLast(new PacketDecoder());
				// 测试环境采用字符串报文
				// pipeline.addLast(new StringEncoder());
				// pipeline.addLast(new StringDecoder());
				// 超时检测 写4s超时检测
				// pipeline.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));
				// 心跳处理
				pipeline.addLast(new ServerHeartBeatRequestHandler(btpLaneService));
				// 业务处理
//				pipeline.addLast(new ServerLaneDeviceHandler(btpLaneService, deviceService));
			}
		};
		tcpServer.setInitializer(initializer);
		return tcpServer;
	}
}
