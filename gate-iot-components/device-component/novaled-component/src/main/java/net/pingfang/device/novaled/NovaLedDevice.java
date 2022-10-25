package net.pingfang.device.novaled;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.network.nova.NovaLed;
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
	private NovaLed novaLed;

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
	public DeviceProduct getProduct() {
		return NovaLedDeviceProduct.NOVA_LED;
	}

	@Override
	public void shutdown() {
		novaLed.shutdown();
	}

	@Override
	public Flux<FunctionMessage> subscribe() {
		return Flux.empty();
	}

	@Override
	public boolean isAlive() {
		return novaLed.isAlive();
	}

	@Override
	public boolean isAutoReload() {
		return false;
	}

	@Override
	public void keepAlive() {
		log.info("短连接设备：" + getDeviceId());
	}


	public void setProperties(NovaLedDeviceProperties properties) {
		this.properties = properties;
	}

	public void setNovaLed(NovaLed novaLed) {
		this.novaLed = novaLed;
	}

	public int setContent(String content) {
		String basic = "[all]\n" //
				+ "items=1\n" //
				+ "[item1]\n" //
				+ "param=100,1,1,1,0,5,1,0,1\n" //
				+ "txt1=0,0,3,1616,1,8,0,%s,32,16,0\n" //
				+ "txtparam1=0,0";
//		NovaTrafficCore core = new NovaTrafficCore(properties.getHost(), properties.getPort());
//		return core.sendPlayList(1, String.format(basic, content));
		return 1;
	}
}
