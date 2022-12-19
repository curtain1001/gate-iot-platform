package net.pingfang.business.tcpserver;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-23 10:50
 */
public class ServerUtils {
	public static String getIp(Channel channel) {
		InetSocketAddress inetSocketAddress = (InetSocketAddress) channel.remoteAddress();
		return inetSocketAddress.getAddress().getHostAddress();
	}

}
