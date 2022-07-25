package net.pingfang.device.licenseplate;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.function.Function;

import com.sun.jna.Pointer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceState;
import net.pingfang.device.licenseplate.values.ImageRecvInfo;
import net.pingfang.device.licenseplate.values.ResultCode;
import net.pingfang.device.licenseplate.values.StatusCode;
import net.pingfang.iot.common.FunctionMessage;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.product.Product;
import net.sdk.bean.basicconfig.reportmess.Data_T_IOStateRsp;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_DCImageSnap.T_DCImageSnap;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_ImageUserInfo.T_ImageUserInfo.ByReference;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_PicInfo.T_PicInfo;
import net.sdk.function.main.NET;
import net.sdk.function.systemcommon.control.message.callback.Callback_FGetReportCBEx.FGetReportCBEx;
import net.sdk.function.systemcommon.imagesnap.callback.Callback_FGetImageCBEx.FGetImageCBEx;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 10:36
 */
@AllArgsConstructor
@Slf4j
public class LicensePlateDevice implements DeviceOperator {
	/**
	 * 句柄：相机在驱动中的位置
	 */
	private int handle = -1;
	final NET net;
	final String deviceId;
	final Long laneId;
	final String deviceName;
	private final EmitterProcessor<FunctionMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<FunctionMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	public LicensePlateDevice(Long laneId, String deviceId, String deviceName, NET net) {
		this.deviceId = deviceId;
		this.laneId = laneId;
		this.deviceName = deviceName;
		this.net = net;
	}

	public void init(String ip, short port, short timeout) {
		// 添加相机
		handle = net.Net_AddCamera(ip);
		int conn = net.Net_ConnCamera(handle, port, timeout);
		if (conn != 0) {
			log.error("相机连接：" + ResultCode.getMsg(conn));
		}
		int rem = net.Net_RegReportMessEx(handle, reportCBEx(), Pointer.NULL);
		if (rem != 0) {
			throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rem));
		}
		int rev = net.Net_RegImageRecvEx(handle, imageCBEx(), Pointer.createConstant(port));
		if (rev != 0) {
			throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rev));
		}
	}

	public int getHandle() {
		if (handle == -1) {
			return -1;
		} else {
			// 判断相机连接状态
			int qcs = net.Net_QueryConnState(handle);
			if (qcs != 0) {
				return -1;
			}
		}
		return handle;
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
	public DeviceState getStatus() {
		return net.Net_QueryConnState(handle) != 0 ? DeviceState.offline : DeviceState.online;
	}

	@Override
	public void shutdown() {
		try {
			int dcc = net.Net_DisConnCamera(handle);
			if (dcc != 0) {
				throw new RuntimeException("注销相机登录：" + ResultCode.getMsg(dcc));
			}
			int del = net.Net_DelCamera(handle);
			if (del != 0) {
				throw new RuntimeException("删除相机失败：" + ResultCode.getMsg(del));
			}
		} finally {
			handle = -1;
			processor.onComplete();
		}
	}

	@Override
	public void setStatus(DeviceState state) {

	}

	@Override
	public Flux<FunctionMessage> subscribe() {
		return processor.map(Function.identity()).cast(FunctionMessage.class);
	}

	@Override
	public void keepAlive() {
		if (net.Net_QueryConnState(handle) == -1) {
		}
	}

	@Override
	public Product getProduct() {
		return LicensePlateProduct.OCR_License_Plate;
	}

	@Override
	public boolean isAlive() {
		return net.Net_QueryConnState(handle) != -1;
	}

	@Override
	public boolean isAutoReload() {
		return true;
	}

	public String saveImageToJpeg(String url) {
		int sitp = net.Net_SaveImageToJpeg(getHandle(), url);
		return StatusCode.getStatusCode(sitp, "Net_SaveImageToJpeg");

	}

	public String imageSnap() {
		T_DCImageSnap.ByReference ptImageSnap = new T_DCImageSnap.ByReference();
		int is = net.Net_ImageSnap(getHandle(), ptImageSnap);
		log.info(StatusCode.getStatusCode(is, "Net_ImageSnap"));
		if (is == 0) {
			log.info("等待回调输出：");
		}
		return StatusCode.getStatusCode(is, "Net_ImageSnap");
	}

	/**
	 * 消息上报回调
	 *
	 * @return
	 */
	public FGetReportCBEx reportCBEx() {
		return new FGetReportCBEx() {
			/**
			 *
			 * @param handle    相机句柄
			 * @param ucType
			 * @param ptMessage
			 * @param pUserData
			 * @return
			 */
			@Override
			public int invoke(int handle, byte ucType, Pointer ptMessage, Pointer pUserData) {
				log.info("通知:{}", ucType);
				if (ucType == 7) {
					Data_T_IOStateRsp.T_IOStateRsp.ByReference tIOStateRsp = new Data_T_IOStateRsp.T_IOStateRsp.ByReference();
					int nSize = tIOStateRsp.size();
					byte[] Readbuf = new byte[nSize];
					ptMessage.read(0, Readbuf, 0, nSize);
					tIOStateRsp.ucIndex = Readbuf[0];
					log.info("io:{}", tIOStateRsp.ucIndex);
				}
				return 0;
			}

		};
	}

	/**
	 * 图片上报回调
	 *
	 * @return
	 */
	public FGetImageCBEx imageCBEx() {
		return new FGetImageCBEx() {
			/**
			 *
			 * @param tHandle     相机句柄
			 * @param uiImageId   当前触发识别的相机句柄图
			 * @param ptImageInfo 识别结果
			 * @param picInfo     图片缓冲区
			 * @param pointer     回调的用户信息
			 * @return
			 */
			@Override
			public int invoke(int tHandle, int uiImageId, ByReference ptImageInfo, T_PicInfo.ByReference picInfo,
					Pointer pointer) {
				try {
					ImageRecvInfo recvInfo = ImageRecvInfo.builder() //
							.acSnapTime(new String(ptImageInfo.acSnapTime))//
							.ucDirection(String.valueOf(ptImageInfo.ucDirection))//
							.ucHaveVehicle("")//
							.ucLaneNo(new String(String.valueOf(ptImageInfo.ucLaneNo)))//
							.szLprResult(new String(ptImageInfo.szLprResult, "GBK").trim())//
							.ucLprType(new String(String.valueOf(ptImageInfo.ucLprType)))//
							.ucPlateColor(String.valueOf(ptImageInfo.ucPlateColor))//
							.ucScore(String.valueOf(ptImageInfo.ucScore))//
							.ucSnapshotIndex(String.valueOf(ptImageInfo.ucSnapshotIndex))//
							.ucSnapType(String.valueOf(ptImageInfo.ucSnapType))//
							.ucTotalNum(String.valueOf(ptImageInfo.ucTotalNum))//
							.ucVehicleBrand(String.valueOf(ptImageInfo.ucVehicleBrand))//
							.ucVehicleColor(String.valueOf(ptImageInfo.ucVehicleColor))//
							.ucVehicleSize(String.valueOf(ptImageInfo.ucVehicleSize))//
							.ucViolateCode(String.valueOf(ptImageInfo.ucViolateCode))//
							.uiVehicleId(String.valueOf(ptImageInfo.uiVehicleId))//
							.usHeight(String.valueOf(ptImageInfo.usHeight))//
							.usLpBox(Arrays.toString(ptImageInfo.usLpBox))//
							.usSpeed(String.valueOf(ptImageInfo.usSpeed)) //
							.usWidth(String.valueOf(ptImageInfo.usWidth))//
							.panorama(picInfo.ptPanoramaPicBuff.getByteArray(0, picInfo.uiPanoramaPicLen)) // 全景图片
							.vehicle(picInfo.ptVehiclePicBuff.getByteArray(0, picInfo.uiVehiclePicLen)) // 车辆图片
							.build();
					// 车牌
					FunctionMessage functionMessage = FunctionMessage.builder() //
							.deviceId(deviceId)//
							.laneId(laneId)//
							.Payload(recvInfo)//
							.product(LicensePlateProduct.OCR_License_Plate)//
							.type(MessagePayloadType.JSON)//
							.build();//

					Mono.just(functionMessage).subscribe(x -> received(x));
				} catch (UnsupportedEncodingException e) {
					log.error("获取车辆识别信息失败", e);
				}
				return 0;
			}
		};
	}

	/**
	 * 接收图像识别消息
	 *
	 * @param functionMessage 图像识别消息
	 */
	protected void received(FunctionMessage functionMessage) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn(" message pending {} ,drop message:{}", processor.getPending(), functionMessage.toString());
			return;
		}
		sink.next(functionMessage);
	}
}
