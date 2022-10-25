package net.pingfang.network.nova.client;

import java.util.Date;
import java.util.List;

import net.pingfang.common.utils.StringUtils;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.nova.DefaultProperties;
import net.pingfang.network.nova.NovaLed;
import net.pingfang.network.nova.media.PlayContent;
import nova.traffic.NovaTraffic;
import nova.traffic.been.DeviceNetBasicParam;
import nova.traffic.been.DeviceNowPlayItem;
import nova.traffic.been.DeviceNowPlayList;
import nova.traffic.been.DevicePowerByTimeParam;
import nova.traffic.been.DeviceSecretParam;
import nova.traffic.been.DeviceType;
import nova.traffic.been.PlayByTimeParam;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-19 14:55
 */

public class NovaClient implements NovaLed {
	private final String id;
	private final Long laneId;
	private NovaTraffic novaTraffic;
	private DefaultProperties defaultProperties;

	public NovaClient(String id, Long laneId, NovaTraffic novaTraffic, DefaultProperties defaultProperties) {
		this.novaTraffic = novaTraffic;
		this.defaultProperties = defaultProperties;
		this.id = id;
		this.laneId = laneId;
		init();
	}

	public void setNovaTraffic(NovaTraffic novaTraffic) {
		this.novaTraffic = novaTraffic;
	}

	public void setDefaultProperties(DefaultProperties defaultProperties) {
		this.defaultProperties = defaultProperties;
	}

	@Override
	public void init() {
		if (defaultProperties.getDeviceName() != null) {
			novaTraffic.setDeviceName(defaultProperties.getDeviceName());
		}
		if (defaultProperties.isDesOpen() && StringUtils.isNotEmpty(defaultProperties.getDesPassword())) {
			novaTraffic.NovaTrafficSetPassword(defaultProperties.getDesPassword());
		}
		DeviceSecretParam secretParam = new DeviceSecretParam(defaultProperties.getPassword(),
				defaultProperties.isDesOpen(), defaultProperties.isMD5Open(), defaultProperties.getDesPassword());
		novaTraffic.setDeviceSecretParam(secretParam);
	}

	@Override
	public String getDeviceName() {
		return novaTraffic.getDeviceName();
	}

	@Override
	public int setDeviceName(String name) {
		return novaTraffic.setDeviceName(name);
	}

	@Override
	public int setDeviceSecretParam(DeviceSecretParam param) {
		return novaTraffic.setDeviceSecretParam(param);
	}

	@Override
	public boolean NovaTrafficSetPassword(String password) {
		return novaTraffic.NovaTrafficSetPassword(password);
	}

	@Override
	public int sendPlayList(int id, String content) {
		return novaTraffic.sendPlayList(id, content);
	}

	@Override
	public int switchPower(boolean power) {
		return novaTraffic.setPower(power);
	}

	@Override
	public int cleanUpFiles(int type) {
		return novaTraffic.cleanUpFiles(type);
	}

	@Override
	public int setBoardPowerAndScreenPower(boolean power) {
		return novaTraffic.setBoardPowerAndScreenPower(power);
	}

	@Override
	public DeviceType getDeviceType() {
		return novaTraffic.getDeviceType();
	}

	@Override
	public int setTime(Date date) {
		return novaTraffic.setTime(date);
	}

	@Override
	public int restart() {
		return novaTraffic.restartDevice();
	}

	@Override
	public void resetDeviceIpToDefault() {
		novaTraffic.resetDeviceIpToDefault();
	}

	@Override
	public void setDeviceNetBasicParam(DeviceNetBasicParam mDeviceBasicParam) {
		novaTraffic.setDeviceNetBasicParam(mDeviceBasicParam);
	}

	@Override
	public DeviceNowPlayItem getNowPlayContent() {
		return novaTraffic.getNowPlayContent();
	}

	@Override
	public DeviceNowPlayList getNowPlayAllContent() {
		return novaTraffic.getNowPlayAllContent();
	}

	@Override
	public int setDevicePowerByTimeList(List<DevicePowerByTimeParam> list) {
		return novaTraffic.setDevicePowerByTimeList(list);
	}

	@Override
	public int setPlayByTimeList(List<PlayByTimeParam> list) {
		return novaTraffic.setPlayByTimeList(list);
	}

	@Override
	public Flux<NetworkMessage> subscribe() {
		return Flux.empty();
	}

	@Override
	public void shutdown() {
	}

	@Override
	public boolean isAlive() {
		return StringUtils.isNotEmpty(novaTraffic.getDeviceName());
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public NetworkType getType() {
		return NovaClientNetworkType.NOVA_CLIENT;
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	private String Convert(List<PlayContent> playContents) {
		StringBuilder content = new StringBuilder();
		content.append("[all]\n");//
		content.append(String.format("items=%s\n", playContents.size())); //
		for (int i = 1; i <= playContents.size(); i++) {
			PlayContent playContent = playContents.get(i);
			content.append(String.format("[item%s]\n", i)); //
			content.append(String.format("param=%s,%s,%s,%s,%s,%s,%s\n", playContent.getDwellTime(),
					playContent.getStartDisplayMode(), playContent.getEndDisplayMode(), playContent.getStartSpeed(),
					playContent.getFlickerSpeed(), playContent.getFlickerNum(), playContent.getPlayNum())); //
			switch (playContent.getPlayType()) {
			case txt:
				content.append(String.format("txt%s=", i));
				break;
			case txtext:
			}
			content.append("txt1=0,0,3,1616,1,8,0,%s,32,16,0\n"); //
			content.append("txtparam1=0,0");
		}
		return content.toString();
	}
}
