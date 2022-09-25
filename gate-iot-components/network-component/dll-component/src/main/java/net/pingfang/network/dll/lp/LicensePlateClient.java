//package net.pingfang.network.dll.lp;
//
//import java.io.UnsupportedEncodingException;
//import java.net.InetSocketAddress;
//import java.util.Arrays;
//
//import com.sun.jna.Pointer;
//
//import io.vertx.core.net.SocketAddress;
//import lombok.extern.slf4j.Slf4j;
//import net.pingfang.iot.common.ClientConnection;
//import net.pingfang.iot.common.EncodedMessage;
//import net.pingfang.iot.common.FunctionMessage;
//import net.pingfang.iot.common.MessagePayloadType;
//import net.pingfang.iot.common.network.NetworkType;
//import net.pingfang.network.Network;
//import net.pingfang.network.dll.lp.values.ImageRecvInfo;
//import net.pingfang.network.dll.lp.values.ResultCode;
//import net.sdk.bean.basicconfig.reportmess.Data_T_IOStateRsp;
//import net.sdk.bean.serviceconfig.imagesnap.Data_T_ImageUserInfo.T_ImageUserInfo.ByReference;
//import net.sdk.bean.serviceconfig.imagesnap.Data_T_PicInfo.T_PicInfo;
//import net.sdk.function.main.NET;
//import net.sdk.function.systemcommon.control.message.callback.Callback_FGetReportCBEx.FGetReportCBEx;
//import net.sdk.function.systemcommon.imagesnap.callback.Callback_FGetImageCBEx.FGetImageCBEx;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * <p>
// *
// * </p>
// *
// * @author 王超
// * @since 2022-09-24 15:05
// */
//@Slf4j
//public class LicensePlateClient implements Network, ClientConnection {
//	private final NET net;
//	private int handle;
//	String ip;
//	int port;
//	long timeout;
//
//	public LicensePlateClient(NET net) {
//		this.handle = -1;
//		this.net = net;
//		init(ip, (short) port, (short) timeout);
//	}
//
//	public void init(String ip, short port, short timeout) {
//		// 添加相机
//		handle = net.Net_AddCamera(ip);
//		int conn = net.Net_ConnCamera(handle, port, timeout);
//		if (conn != 0) {
//			log.error("相机连接：" + ResultCode.getMsg(conn));
//		}
//		int rem = net.Net_RegReportMessEx(handle, reportCBEx(), Pointer.NULL);
//		if (rem != 0) {
//			throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rem));
//		}
//		int rev = net.Net_RegImageRecvEx(handle, imageCBEx(), Pointer.createConstant(port));
//		if (rev != 0) {
//			throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rev));
//		}
//	}
//
//	@Override
//	public InetSocketAddress address() {
//		return getRemoteAddress();
//	}
//
//	public InetSocketAddress getRemoteAddress() {
//		if (null == socket) {
//			return null;
//		}
//		SocketAddress socketAddress = socket.remoteAddress();
//		return new InetSocketAddress(socketAddress.host(), socketAddress.port());
//	}
//
//	@Override
//	public Mono<Void> sendMessage(EncodedMessage message) {
//		return null;
//	}
//
//	@Override
//	public Flux<EncodedMessage> receiveMessage() {
//		return null;
//	}
//
//	@Override
//	public void disconnect() {
//
//	}
//
//	@Override
//	public String getId() {
//		return null;
//	}
//
//	@Override
//	public NetworkType getType() {
//		return null;
//	}
//
//	@Override
//	public void shutdown() {
//
//	}
//
//	@Override
//	public boolean isAlive() {
//		return false;
//	}
//
//	@Override
//	public boolean isAutoReload() {
//		return false;
//	}
//
//	/**
//	 * 消息上报回调
//	 *
//	 * @return
//	 */
//	public FGetReportCBEx reportCBEx() {
//		return new FGetReportCBEx() {
//			/**
//			 *
//			 * @param handle    相机句柄
//			 * @param ucType
//			 * @param ptMessage
//			 * @param pUserData
//			 * @return
//			 */
//			@Override
//			public int invoke(int handle, byte ucType, Pointer ptMessage, Pointer pUserData) {
//				log.info("通知:{}", ucType);
//				if (ucType == 7) {
//					Data_T_IOStateRsp.T_IOStateRsp.ByReference tIOStateRsp = new Data_T_IOStateRsp.T_IOStateRsp.ByReference();
//					int nSize = tIOStateRsp.size();
//					byte[] Readbuf = new byte[nSize];
//					ptMessage.read(0, Readbuf, 0, nSize);
//					tIOStateRsp.ucIndex = Readbuf[0];
//					log.info("io:{}", tIOStateRsp.ucIndex);
//				}
//				return 0;
//			}
//
//		};
//	}
//
//	/**
//	 * 图片上报回调
//	 *
//	 * @return
//	 */
//	public FGetImageCBEx imageCBEx() {
//		return new FGetImageCBEx() {
//			/**
//			 *
//			 * @param tHandle     相机句柄
//			 * @param uiImageId   当前触发识别的相机句柄图
//			 * @param ptImageInfo 识别结果
//			 * @param picInfo     图片缓冲区
//			 * @param pointer     回调的用户信息
//			 * @return
//			 */
//			@Override
//			public int invoke(int tHandle, int uiImageId, ByReference ptImageInfo, T_PicInfo.ByReference picInfo,
//					Pointer pointer) {
//				try {
//					log.info("车牌上报：");
//					ImageRecvInfo recvInfo = ImageRecvInfo.builder() //
//							.acSnapTime(new String(ptImageInfo.acSnapTime))//
//							.ucDirection(String.valueOf(ptImageInfo.ucDirection))//
//							.ucHaveVehicle("")//
//							.ucLaneNo(String.valueOf(ptImageInfo.ucLaneNo))//
//							.szLprResult(new String(ptImageInfo.szLprResult, "GBK").trim())//
//							.ucLprType(String.valueOf(ptImageInfo.ucLprType))//
//							.ucPlateColor(String.valueOf(ptImageInfo.ucPlateColor))//
//							.ucScore(String.valueOf(ptImageInfo.ucScore))//
//							.ucSnapshotIndex(String.valueOf(ptImageInfo.ucSnapshotIndex))//
//							.ucSnapType(String.valueOf(ptImageInfo.ucSnapType))//
//							.ucTotalNum(String.valueOf(ptImageInfo.ucTotalNum))//
//							.ucVehicleBrand(String.valueOf(ptImageInfo.ucVehicleBrand))//
//							.ucVehicleColor(String.valueOf(ptImageInfo.ucVehicleColor))//
//							.ucVehicleSize(String.valueOf(ptImageInfo.ucVehicleSize))//
//							.ucViolateCode(String.valueOf(ptImageInfo.ucViolateCode))//
//							.uiVehicleId(String.valueOf(ptImageInfo.uiVehicleId))//
//							.usHeight(String.valueOf(ptImageInfo.usHeight))//
//							.usLpBox(Arrays.toString(ptImageInfo.usLpBox))//
//							.usSpeed(String.valueOf(ptImageInfo.usSpeed)) //
//							.usWidth(String.valueOf(ptImageInfo.usWidth))//
//							.panorama(picInfo.ptPanoramaPicBuff.getByteArray(0, picInfo.uiPanoramaPicLen)) // 全景图片
//							.vehicle(picInfo.ptVehiclePicBuff.getByteArray(0, picInfo.uiVehiclePicLen)) // 车辆图片
//							.build();
//					// 车牌
//					FunctionMessage functionMessage = FunctionMessage.builder() //
//							.deviceId(deviceId)//
//							.laneId(laneId)//
//							.Payload(recvInfo)//
//							.product(LicensePlateProduct.OCR_License_Plate)//
//							.type(MessagePayloadType.JSON)//
//							.build();//
//					Mono.just(functionMessage).subscribe(x -> received(x));
//
//					RecvReport recvReport = (RecvReport) instructionManager.getInstruction("RECV_REPORT");
//					recvReport.received(functionMessage);
//				} catch (UnsupportedEncodingException e) {
//					log.error("获取车辆识别信息失败", e);
//				}
//				return 0;
//			}
//		};
//	}
//
//}
