package net.pingfang.device.novaled.core;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.novaled.NovaLedDeviceProduct;
import net.pingfang.device.novaled.NovaLedDeviceProperties;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.product.Product;
import reactor.core.publisher.Flux;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-17 10:10
 */
@Slf4j
public class NovaLedDevice implements DeviceOperator {
	final Long laneId;
	final String deviceId;
	final String deviceName;
	private NovaLedDeviceProperties properties;
//	private TcpClient tcpClient;
//	public NetworkManager networkManager;

	public NovaLedDevice(Long laneId, String deviceId, String deviceName, NovaLedDeviceProperties properties) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.properties = properties;
	}

	@Override
	public String getDeviceId() {
		return this.deviceId;
	}

	@Override
	public Long getLaneId() {
		return this.laneId;
	}

	@Override
	public String getDeviceName() {
		return this.deviceName;
	}

	@Override
	public Product getProduct() {
		return NovaLedDeviceProduct.NOVA_LED;
	}

	@Override
	public void shutdown() {

	}

	@Override
	public Flux<FunctionMessage> subscribe(Long laneId) {
//		return tcpClient.subscribe().map(x -> new FunctionMessage(laneId, deviceId, NovaLedDeviceProduct.NOVA_LED,
//				x.getPayload(), MessagePayloadType.BINARY));
		return Flux.empty();
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public boolean isAutoReload() {
		return false;
	}

	@Override
	public void keepAlive() {
		log.info("短连接设备：" + getDeviceId());
	}
//
//	public void setTcpClient(Map<String, Object> properties) {
//		// 粘黏包处理设置
//		properties.put("parserType", PayloadParserType.LED);
//
//		NetworkProperties networkProperties = new NetworkProperties();
//		networkProperties.setId(deviceId);
//		networkProperties.setName(deviceName);
//		networkProperties.setEnabled(true);
//		networkProperties.setConfigurations(properties);
//		this.tcpClient = (TcpClient) networkManager.getNetwork(DefaultNetworkType.TCP_CLIENT, networkProperties,
//				deviceId);
//	}

	public void setProperties(NovaLedDeviceProperties properties) {
		this.properties = properties;
	}

	public int setContent(String content) {
		String basic = "[all]\n" //
				+ "items=1\n" //
				+ "[item1]\n" //
				+ "param=100,1,1,1,0,5,1,0,1\n" //
				+ "txt1=0,0,3,1616,1,8,0,%s,32,16,0\n" //
				+ "txtparam1=0,0";
		NovaTrafficCore core = new NovaTrafficCore(properties.getHost(), properties.getPort());
		return core.sendPlayList(1, String.format(basic, content));
	}
}
