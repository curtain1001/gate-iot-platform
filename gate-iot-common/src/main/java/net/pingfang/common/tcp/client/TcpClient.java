package net.pingfang.common.tcp.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-03 15:15
 */
@Slf4j
public class TcpClient {
	private final String ip;
	private final int port;
	private ChannelInitializer<SocketChannel> initializer;
	@Getter
	private ChannelFuture channelFuture = null;
	private EventLoopGroup group = null;

	public TcpClient(String ip, int port) {
		this.ip = ip;
		this.port = port;

	}

	public void setInitializer(ChannelInitializer<SocketChannel> initializer) {
		this.initializer = initializer;
	}

	public boolean connect() {
		try {
			this.group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(this.group) //
					.channel(NioSocketChannel.class) //
					.option(ChannelOption.TCP_NODELAY, true) //
					.handler(initializer); //
			channelFuture = bootstrap.connect(ip, port).addListener((ChannelFutureListener) future -> {
				if (!future.isSuccess()) {
					future.channel().eventLoop().schedule(() -> {
						log.debug("正在重新连接服务端 目标 ip：{}， port：{}", ip, port);
						connect();
					}, 5, TimeUnit.SECONDS);
				}
			}).sync();
			channelFuture.channel().closeFuture().sync();
			return true;
		} catch (Exception e) {
			log.error("错误", e);
			return false;
		}
	}

	public void send(Object o) {
		this.channelFuture.channel().writeAndFlush(o);
	}

	public void shutdown() {
		this.channelFuture.channel().close();
		this.group.shutdownGracefully();
	}

}
