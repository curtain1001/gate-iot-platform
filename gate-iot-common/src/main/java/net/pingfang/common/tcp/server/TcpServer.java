package net.pingfang.common.tcp.server;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-07 11:24
 */
@Slf4j
public class TcpServer implements Runnable {

	private final String ip;
	private final int port;
	private ChannelFuture channelFuture;
	private EventLoopGroup boss;
	private EventLoopGroup work;

	public TcpServer(int port) {
		this.ip = "127.0.0.1";
		this.port = port;
	}

	private ChannelInitializer<SocketChannel> initializer;

	public void setInitializer(ChannelInitializer<SocketChannel> initializer) {
		this.initializer = initializer;
	}

	private void start() {
		this.boss = new NioEventLoopGroup();
		this.work = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		try {
			serverBootstrap.group(boss, work) //
					.channel(NioServerSocketChannel.class) //
					.option(ChannelOption.SO_BACKLOG, 1024 * 1024) //
					.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024 * 1024))// 这行配置比较重要
					.childHandler(initializer) //
					.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1, 1024 * 1024 * 8)); //
			this.channelFuture = serverBootstrap.bind(port).addListener((ChannelFutureListener) future -> {
				if (!future.isSuccess()) {
					future.channel().eventLoop().schedule(() -> {
						log.debug("正在重新创建服务端 目标 port：{}", port);
						start();
					}, 5, TimeUnit.SECONDS);
				}
				log.info("TCP服务启动完成");
			}).sync();
			this.channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("错误", e);
		}
	}

	public void send(ChannelFuture channelFuture, Object o) {
		channelFuture.channel().writeAndFlush(o);
	}

	public void shutdown() {
		Thread.currentThread().interrupt();
		this.channelFuture.channel().close();
		this.boss.shutdownGracefully();
		this.work.shutdownGracefully();
	}

	@Override
	public void run() {
		start();
	}
}
