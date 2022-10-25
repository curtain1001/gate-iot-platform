package net.pingfang.network.dll.lp;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.function.Function;

import com.sun.jna.Pointer;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.iot.common.MessagePayloadType;
import net.pingfang.iot.common.NetworkMessage;
import net.pingfang.iot.common.NetworkSession;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.Network;
import net.pingfang.network.dll.lp.config.SdkNet;
import net.pingfang.network.dll.lp.values.ImageRecvInfo;
import net.pingfang.network.dll.lp.values.ResultCode;
import net.sdk.bean.basicconfig.reportmess.Data_T_IOStateRsp;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_ImageUserInfo.T_ImageUserInfo.ByReference;
import net.sdk.bean.serviceconfig.imagesnap.Data_T_PicInfo.T_PicInfo;
import net.sdk.function.main.NET;
import net.sdk.function.systemcommon.control.message.callback.Callback_FGetReportCBEx.FGetReportCBEx;
import net.sdk.function.systemcommon.imagesnap.callback.Callback_FGetImageCBEx.FGetImageCBEx;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-24 15:05
 */
@Slf4j
public class LicensePlateClient implements Network, NetworkSession {
	private final String id;
	private final NET net;
	private int handle;
	private final String deviceId;
	private final Long laneId;

	private final EmitterProcessor<NetworkMessage> processor = EmitterProcessor.create(false);
	private final FluxSink<NetworkMessage> sink = processor.sink(FluxSink.OverflowStrategy.BUFFER);

	public LicensePlateClient(String id, Long laneId) {
		this.handle = -1;
		this.id = id;
		this.deviceId = id;
		this.laneId = laneId;
		this.net = SdkNet.net;
	}

	public String getId() {
		return this.id;
	}

	public NetworkType getType() {
		return LpDllNetworkType.LP_DLL;
	}

	@Override
	public boolean isAutoReload() {
		return false;
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
	public Flux<NetworkMessage> subscribe() {
		return processor.map(Function.identity());
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
		}
	}
	@Override
	public boolean isAlive() {
		return net.Net_QueryConnState(handle) == 0;
	}


	public void setHandle(int handle) {
		this.handle = handle;
	}

	/**
	 * 接收消息
	 *
	 * @param message 消息
	 */
	protected void received(NetworkMessage message) {
		if (processor.getPending() > processor.getBufferSize() / 2) {
			log.warn("tcp server [{}],drop message:{}", processor.getPending(), message.toString());
			return;
		}
		sink.next(message);
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
					log.info("车牌上报：");
					ImageRecvInfo recvInfo = ImageRecvInfo.builder() //
							.acSnapTime(new String(ptImageInfo.acSnapTime))//
							.ucDirection(String.valueOf(ptImageInfo.ucDirection))//
							.ucHaveVehicle("")//
							.ucLaneNo(String.valueOf(ptImageInfo.ucLaneNo))//
							.szLprResult(new String(ptImageInfo.szLprResult, "GBK").trim())//
							.ucLprType(String.valueOf(ptImageInfo.ucLprType))//
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
					NetworkMessage message = NetworkMessage.builder()//
							.laneId(laneId)//
							.deviceId(deviceId)//
							.payload(JsonUtils.toJsonString(recvInfo))//
							.payloadType(MessagePayloadType.JSON)//
							.networkType(LpDllNetworkType.LP_DLL)//
							.build();
					received(message);
				} catch (UnsupportedEncodingException e) {
					log.error("获取车辆识别信息失败", e);
				}
				return 0;
			}
		};
	}

}
