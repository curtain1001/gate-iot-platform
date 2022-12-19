package net.pingfang.business.device;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.Maps;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-11 15:07
 */
@Slf4j
public class LaneDeviceSupport {
	private static final ConcurrentMap<String, Channel> laneNoChannel = Maps.newConcurrentMap();

	public static void addLane(String laneNo, Channel channel) {
		laneNoChannel.put(laneNo, channel);
		log.info("车道{}:客户端连接：{}", laneNo, channel.id().asLongText());
	}

	public static void remove(Channel channel) {
		String laneNo = getLaneNo(channel.id().asLongText());
		if (laneNo != null) {
			laneNoChannel.remove(laneNo);
			log.info("车道{}:客户端断开连接：{}", laneNo, channel.id().asLongText());
		}
	}

	public static Channel getLaneChannel(String laneNo) {
		return laneNoChannel.get(laneNo);
	}

	public static void sendMsg(String laneNo, Object o) {
		Channel channel = laneNoChannel.get(laneNo);
		if (channel != null) {
			channel.writeAndFlush(o);
		}
	}

	public static void sendAll(Object o) {
		laneNoChannel.values().stream().filter(Objects::nonNull).forEach(x -> x.writeAndFlush(o));
	}

	public static String getLaneNo(String channelId) {
		Optional<Entry<String, Channel>> entry = laneNoChannel.entrySet().stream()
				.filter(x -> channelId.equals(x.getValue().id().asLongText())).findFirst();
		return entry.map(Entry::getKey).orElse(null);
	}

}
