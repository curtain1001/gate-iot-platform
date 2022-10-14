package net.pingfang.device.licenseplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.licenseplate.values.StatusCode;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.dll.lp.LpClient;
import net.pingfang.network.dll.lp.config.SdkNet;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_DCImageSnap.T_DCImageSnap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 10:36
 */
@AllArgsConstructor
@Slf4j
public class LicensePlateDevice implements DeviceOperator {
	final Long laneId;
	final String deviceId;
	final String deviceName;
	private LpClient client;
	final NetworkManager networkManager;

	public LicensePlateDevice(Long laneId, String deviceId, String deviceName, NetworkManager networkManager) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.networkManager = networkManager;
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
	public void shutdown() {
		networkManager.shutdown(DefaultNetworkType.LP_DLL, deviceId);
	}

	@Override
	public Flux<FunctionMessage> subscribe(Long laneId) {
		return client.subscribe().map(x -> FunctionMessage.builder()//
				.type(x.getPayloadType())//
				.Payload(x.getPayload())//
				.deviceId(deviceId)//
				.product(LicensePlateProduct.OCR_License_Plate)//
				.laneId(laneId)//
				.build()).filterWhen(x -> {
					if (laneId != null) {
						return Mono.just(laneId.equals(x.getLaneId()));
					} else {
						return Mono.just(true);
					}
				});
	}

	@Override
	public void keepAlive() {
		if (isAlive()) {
			// todo 保持连接
		}
	}

	@Override
	public Product getProduct() {
		return LicensePlateProduct.OCR_License_Plate;
	}

	@Override
	public boolean isAlive() {
		return client.isAlive();
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	public int saveImageToJpeg(String url) {
		int sitp = SdkNet.net.Net_SaveImageToJpeg(client.getHandle(), url);
		log.info(StatusCode.getStatusCode(sitp, "Net_SaveImageToJpeg"));
		return sitp;

	}

	public int imageSnap() {
		T_DCImageSnap.ByReference ptImageSnap = new T_DCImageSnap.ByReference();
		int is = SdkNet.net.Net_ImageSnap(client.getHandle(),new T_DCImageSnap.ByReference());
		log.info(StatusCode.getStatusCode(is, "Net_ImageSnap"));
		if (is == 0) {
			log.info("等待回调输出：");
		}
		return is;
	}

	public void setLpClient(LpClient lpClient) {
		this.client = lpClient;
	}

}
