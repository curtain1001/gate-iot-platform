package net.pingfang.device.novaled.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.novaled.domain.BrightnessByTimeParam;
import net.pingfang.device.novaled.domain.BrightnessItem;
import net.pingfang.device.novaled.domain.DeviceMemorySize;
import net.pingfang.device.novaled.domain.DeviceNTPParam;
import net.pingfang.device.novaled.domain.DeviceNetBasicParam;
import net.pingfang.device.novaled.domain.DeviceNowPlayItem;
import net.pingfang.device.novaled.domain.DeviceNowPlayList;
import net.pingfang.device.novaled.domain.DevicePowerByTimeParam;
import net.pingfang.device.novaled.domain.DeviceSecretParam;
import net.pingfang.device.novaled.domain.DeviceSize;
import net.pingfang.device.novaled.domain.DeviceType;
import net.pingfang.device.novaled.domain.PlayByTimeParam;
import net.pingfang.device.novaled.domain.TimeZoneParam;
import net.pingfang.device.novaled.utils.FileUtils;
import net.pingfang.device.novaled.utils.NovaUtils;
import net.pingfang.device.novaled.utils.ResultCode;

@Slf4j
public class NovaTrafficCore {
	private static final int SUCCESS = 1;

	private static final int TEMP_SIZE = 100;
	private static final int BIG_SIZE = 4096;
	private static final int MAX_FAIL_COUNT = 10;

	private String ip;
	private int port;
	private Socket mSocket;
	private String mDesStr;
	private ProtocolParser mProtocolParser;

	/**
	 * 初始化通用接口,此方法可以用来公网监听端口建立连接
	 *
	 * @param socket socket
	 */
	public NovaTrafficCore(Socket socket) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			log.error("异常：", e);
		}
		mSocket = socket;
		mProtocolParser = null;
	}

	/**
	 * 初始化通用接口
	 *
	 * @param ip
	 * @param port
	 */
	public NovaTrafficCore(String ip, int port) {
		this.ip = ip;
		this.port = port;
		mProtocolParser = null;
	}

	private byte[] getBytes(String str) throws UnsupportedEncodingException {
		return str.getBytes(StandardCharsets.UTF_8);
	}

	public String getDeviceName() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[1];
			bytes[0] = 1;
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_DEVICE_NAME_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 发送文件名回传.
			byte[] recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_DEVICE_NAME_RSP);
			if (recvBuf != null && recvBuf[1] == SUCCESS) {
				return new String(recvBuf, 2, recvBuf.length - 2);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeSocket(socket);
		}

		return null;
	}

	public int setDeviceName(String name) {
		byte[] nameBytes;
		try {
			nameBytes = getBytes(name);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}
		byte[] bytes = new byte[1 + nameBytes.length];
		bytes[0] = 0;
		System.arraycopy(nameBytes, 0, bytes, 1, nameBytes.length);

		return normalSet(NovaUtils.WHAT_DEVICE_NAME_REQ, NovaUtils.WHAT_DEVICE_NAME_RSP, bytes);
	}

	/**
	 * 收到内容 和 发送内容加密
	 *
	 * @param password
	 * @return
	 */
	public boolean NovaTrafficSetPassword(String password) {
		if (password != null && password.getBytes().length == 8) {
			mDesStr = password;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 用于客户端短连接建立socket，长连接模式直接赋值
	 *
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private Socket getSocket() throws UnknownHostException, IOException {
		if (mSocket == null) {
			return new Socket(ip, port);
		} else {
			if (!mSocket.isClosed() && mSocket.isConnected()) {
				return mSocket;
			} else {
				return null;
			}
		}

	}

	/**
	 * 用于客户端短连接关闭socket，长连接模式不关闭
	 *
	 * @param socket
	 */
	private void closeSocket(Socket socket) {
		if (mSocket == null) {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 用于发送加密
	 *
	 * @param what
	 * @param data
	 * @param dataSize
	 * @return
	 */
	private byte[] makeSendPacket(byte what, byte[] data, int dataSize) {
		if (mDesStr != null) {
			return NovaUtils.makeSendPacket(what, data, dataSize, true, mDesStr.getBytes());
		} else {
			return NovaUtils.makeSendPacket(what, data, dataSize);
		}
	}

	/**
	 * 发送播放文件。 content例子 "[all]\r\n" + "items=2\r\n" + "[item1]\r\n" +
	 * "param=30,1,1,1,0,5,1,0,1\r\n" + "txt1=0,0,3,2121,1,8,0,123,0,0,0\r\n" +
	 * "txtparam1=0,0\r\n" +
	 * "time1=0,0,0,0,30,1,00000000,1616,3,0,1,0,1,1,1,1,1,1,1,1,1\r\n" +
	 * "[item2]\r\n" + "param=4,1,1,1,0,5,1,0,1\r\n" +
	 * "txt1=0,0,3,1616,1,8,0,456,0,0,0\r\n" + "txtparam1=0,0";
	 *
	 * @param id      播放文件id 1 代表play001.lst文件
	 * @param content 播放内容 utf-8
	 * @return
	 */
	public int sendPlayList(int id, String content) {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			String playname = "play" + String.format("%03d", id) + ".lst";
			// 发送文件名
			byte[] bytes = new byte[playname.getBytes().length + 2];
			bytes[0] = (byte) 0xff;
			bytes[1] = (byte) 0xff;
			System.arraycopy(playname.getBytes(), 0, bytes, 2, playname.getBytes().length);

			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_FILENAME_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);
			byte[] inputBuf = new byte[TEMP_SIZE];

			// 发送文件名回传.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILENAME_RSP);
			if (recvBuf == null) {
				return ResultCode.SEND_FILE_NAME_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_FILE_NAME_RECV_ERROR.getKey();
			}

			// 发送文件块 1块
			bytes = new byte[getBytes(content).length + 2];
			NovaUtils.shortToBytes(bytes, 0, (short) 1);
			System.arraycopy(getBytes(content), 0, bytes, 2, getBytes(content).length);
			outbuf = makeSendPacket(NovaUtils.WHAT_FILESEND_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_RSP);
			if (recvBuf == null) {
				return ResultCode.SEND_FILE_BLOCK_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[3] != SUCCESS) {
				return ResultCode.SEND_FILE_BLOCK_RECV_ERROR.getKey();
			}

			recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_OVER_RSP);
			if (recvBuf == null) {
				return ResultCode.SEND_FILE_BLOCK_NOT_RECV_END_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_FILE_BLOCK_RECV_END_ERROR.getKey();
			}

			// 指定播放
			byte[] tempbuf = new byte[1];
			tempbuf[0] = (byte) id;
			outbuf = makeSendPacket(NovaUtils.WHAT_PLAYBYLIST_REQ, tempbuf, tempbuf.length);
			socket.getOutputStream().write(outbuf);

			recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_PLAYBYLIST_RSP);
			if (recvBuf == null) {
				return ResultCode.PLAY_LIST_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.PLAY_LIST_RECV_ERROR.getKey();
			}

			return SUCCESS;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 发送局部更新。 content例子 "[all]\r\n" + "items=2\r\n" + "[item1]\r\n" +
	 * "param=30,1,1,1,0,5,1,0,1\r\n" + "txt1=0,0,3,2121,1,8,0,123,0,0,0\r\n" +
	 * "txtparam1=0,0\r\n" +
	 * "time1=0,0,0,0,30,1,00000000,1616,3,0,1,0,1,1,1,1,1,1,1,1,1\r\n" +
	 * "[item2]\r\n" + "param=4,1,1,1,0,5,1,0,1\r\n" +
	 * "txt1=0,0,3,1616,1,8,0,456,0,0,0\r\n" + "txtparam1=0,0";
	 *
	 * @param id      局部更新id
	 * @param content 播放内容 utf-8
	 * @return
	 */
	public int sendLocalUpdate(int id, String content) {
		byte[] bytes = null;
		try {
			bytes = new byte[getBytes(content).length + 2];
			bytes[0] = 1;
			bytes[1] = (byte) id;
			System.arraycopy(getBytes(content), 0, bytes, 2, getBytes(content).length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}

		return normalSet(NovaUtils.WHAT_REGION_UPDATE_REQ, NovaUtils.WHAT_REGION_UPDATE_RSP, bytes);
	}

	/**
	 * 移除某个id的局部更新
	 *
	 * @param id 移除局部更新id
	 * @return
	 */
	public int removeLocalUpdate(int id) {
		byte[] bytes = new byte[2];
		bytes[0] = 0;
		bytes[1] = (byte) id;
		return normalSet(NovaUtils.WHAT_REGION_UPDATE_REQ, NovaUtils.WHAT_REGION_UPDATE_RSP, bytes);
	}

	/**
	 * 发送文件 NovaTrafficDevice.sendFile(new File("d:\\001.jpg"), "001.jpg",
	 * "192.168.0.220", 5000);
	 *
	 * @param file         本地发送文件名
	 * @param saveFileName 卡上保存的文件名 utf-8编码
	 * @return
	 */
	public int sendFile(File file, String saveFileName) {
		Socket socket = null;
		try {
			int num = 1;
			int outSize = 65535;
			byte[] outBuf;
			FileInputStream fis = null;
			if (file.isFile()) {
				socket = getSocket();
				if (socket == null) {
					return ResultCode.SOCKET_ERROR.getKey();
				}

				// 发送文件名
				byte[] bytes = new byte[getBytes(saveFileName).length + 2];
				bytes[0] = (byte) 0xff;
				bytes[1] = (byte) 0xff;
				System.arraycopy(getBytes(saveFileName), 0, bytes, 2, getBytes(saveFileName).length);

				byte[] outbuf = makeSendPacket(NovaUtils.WHAT_FILENAME_REQ, bytes, bytes.length);
				socket.getOutputStream().write(outbuf);
				byte[] inputBuf = new byte[TEMP_SIZE];

				// 发送文件名回传.
				byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILENAME_RSP);
				if (recvBuf == null) {
					return ResultCode.SEND_FILE_NAME_NOT_RECV_ERROR.getKey();
				} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
					return ResultCode.SEND_FILE_NAME_RECV_ERROR.getKey();
				}

				// 以字节流方法读取文件
				try {
					fis = new FileInputStream(file);
					// 设置一个，每次 装载信息的容器
					byte[] buffer = new byte[outSize];
					// 开始读取数据
					int fileLen = 0;// 每次读取到的数据的长度
					int lastFileCopySize = 0;
					while ((fileLen = fis.read(buffer)) != -1) {// len值为-1时，表示没有数据了
						// append方法往sb对象里面添加数据
						outBuf = new byte[fileLen + 2];
						short id = (short) (num++ & 0xffff);
						NovaUtils.shortToBytes(outBuf, 0, id);
						System.arraycopy(buffer, 0, outBuf, 2, fileLen);
						lastFileCopySize = fileLen;
						byte[] buf = makeSendPacket(NovaUtils.WHAT_FILESEND_REQ, outBuf, outBuf.length);
						socket.getOutputStream().write(buf);

						recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_RSP);
						if (recvBuf == null) {
							return ResultCode.SEND_FILE_BLOCK_NOT_RECV_ERROR.getKey();
						} else if (recvBuf.length >= 2 && recvBuf[3] != SUCCESS) {
							return ResultCode.SEND_FILE_BLOCK_RECV_ERROR.getKey();
						}

					}

					// 当发送文件为outSize的整数倍时，发送结束标识
					if (lastFileCopySize == outSize) {
						outBuf = new byte[2];
						short id = (short) (num++ & 0xffff);
						NovaUtils.shortToBytes(outBuf, 0, id);

						byte[] buf = makeSendPacket(NovaUtils.WHAT_FILESEND_REQ, outBuf, outBuf.length);
						socket.getOutputStream().write(buf);

						recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_RSP);
						if (recvBuf == null) {
							return ResultCode.SEND_FILE_BLOCK_NOT_RECV_ERROR.getKey();
						} else if (recvBuf.length >= 2 && recvBuf[3] != SUCCESS) {
							return ResultCode.SEND_FILE_BLOCK_RECV_ERROR.getKey();
						}

						recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_OVER_RSP);
						if (recvBuf == null) {
							return ResultCode.SEND_FILE_BLOCK_NOT_RECV_END_ERROR.getKey();
						} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
							return ResultCode.SEND_FILE_BLOCK_RECV_END_ERROR.getKey();
						}
						return SUCCESS;
					}

					recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_FILESEND_OVER_RSP);
					if (recvBuf == null) {
						// 如果没有粘包再来一次
						return ResultCode.SEND_FILE_BLOCK_NOT_RECV_END_ERROR.getKey();
					} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
						return ResultCode.SEND_FILE_BLOCK_RECV_END_ERROR.getKey();
					}
					// 输出字符串
				} catch (IOException e) {
					e.printStackTrace();
					return ResultCode.IO_ERROR.getKey();
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else {
				System.out.println("文件不存在！");
				return ResultCode.FILE_NOT_EXIST_ERROR.getKey();
			}

			return SUCCESS;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置手动亮度调节
	 *
	 * @param brightness 0 - 255
	 * @return
	 */
	public int setBrightness(int brightness) {
		if (brightness < 0 || brightness > 255) {
			brightness = 125;
		}

		byte[] bytes = new byte[2];
		bytes[0] = 2; // 手动
		bytes[1] = (byte) brightness;
		return normalSet(NovaUtils.WHAT_SCREEN_BRIGHTNESS_MODE_REQ, NovaUtils.WHAT_SCREEN_BRIGHTNESS_MODE_RSP, bytes);

	}

	/**
	 * 设置开关屏
	 *
	 * @param power true开 fasle 关
	 * @return
	 */
	public int setPower(boolean power) {
		byte[] bytes = new byte[1];
		bytes[0] = power ? (byte) 1 : 0;
		return normalSet(NovaUtils.WHAT_SCREEN_POWER_REQ, NovaUtils.WHAT_SCREEN_POWER_RSP, bytes);
	}

	/**
	 * 清理文件
	 *
	 * @param type 0-清理所有媒体文件 1-清理无效媒体文 件 2-清理所有文件 3-清理升级包
	 * @return
	 */
	public int cleanUpFiles(int type) {
		if (type < 0 || type > 3) {
			type = 1;
		}
		byte[] bytes = new byte[1];
		bytes[0] = (byte) type;
		return normalSet(NovaUtils.WHAT_CLEAN_PLAYLIST_REQ, NovaUtils.WHAT_CLEAN_PLAYLIST_RSP, bytes);
	}

	/**
	 * 设置自动亮度调节
	 *
	 * @param brightnessItem 必须是8组
	 * @return
	 */
	public int setBrightnessAuto(List<BrightnessItem> brightnessItem) {
		int brightSize = 8;
		if (brightnessItem == null || brightnessItem.size() != brightSize) {
			return ResultCode.PARAM_ERROR.getKey();
		}

		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			// 设置自动
			byte[] bytes = new byte[2];
			bytes[0] = 1; // 自动
			bytes[1] = 0;
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_BRIGHTNESS_MODE_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			bytes = new byte[24];
			for (int i = 0; i < brightSize; i++) {
				bytes[i * 3] = (byte) (0xff & brightnessItem.get(i).getEnvironment());
				bytes[i * 3 + 1] = (byte) (0xff & brightnessItem.get(i).getScreen());
				bytes[i * 3 + 2] = 0;
			}

			outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_BRIGHTNESS_SET_AUTO_MODE_PARAM_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf,
					NovaUtils.WHAT_SCREEN_BRIGHTNESS_SET_AUTO_MODE_PARAM_RSP);

			if (recvBuf == null) {
				return ResultCode.SEND_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_RECV_ERROR.getKey();
			}

			return SUCCESS;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 获取自动亮度调节
	 *
	 * @return
	 */
	public List<BrightnessItem> getBrightnessAuto() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			// 设置自动
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_BRIGHTNESS_GET_AUTO_MODE_PARAM_REQ, bytes,
					bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf,
					NovaUtils.WHAT_SCREEN_BRIGHTNESS_GET_AUTO_MODE_PARAM_RSP);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 25) {
				return null;
			}

			List<BrightnessItem> brightnessItem = new ArrayList<BrightnessItem>();
			for (int i = 0; i < 8; i++) {
				BrightnessItem item = new BrightnessItem(0xff & recvBuf[1 + i * 3], 0xff & recvBuf[2 + i * 3]);
				brightnessItem.add(item);
			}
			return brightnessItem;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 控制本板电源和屏幕黑屏
	 *
	 * @return
	 */
	public int setBoardPowerAndScreenPower(boolean power) {
		byte[] bytes = new byte[1];
		bytes[0] = power ? (byte) 1 : 0;
		return normalSet(NovaUtils.WHAT_SCREEN_MY_BOARD_POWER_AND_POWER_REQ,
				NovaUtils.WHAT_SCREEN_MY_BOARD_POWER_AND_POWER_RSP, bytes);
	}

	/**
	 * 控制本板电源
	 *
	 * @return
	 */
	public int setBoardPower(boolean power) {
		byte[] bytes = new byte[1];
		bytes[0] = power ? (byte) 1 : 0;
		return normalSet(NovaUtils.WHAT_SCREEN_MY_BOARD_POWER_REQ, NovaUtils.WHAT_SCREEN_MY_BOARD_POWER_RSP, bytes);
	}

	/**
	 * 控制多功能卡电源
	 *
	 * @param cardIndex 0-255 多功能卡位置索引
	 * @param roadIndex 0-7 电源路数索引
	 * @param power
	 * @return
	 */
	public int setMultiFunctionCardPower(int cardIndex, int roadIndex, boolean power) {
		byte[] bytes = new byte[3];
		bytes[0] = (byte) (0xff & cardIndex);
		bytes[1] = (byte) (0xff & roadIndex);
		bytes[2] = power ? (byte) 1 : 0;
		return normalSet(NovaUtils.WHAT_FUNCTION_CARD_POWER_REQ, NovaUtils.WHAT_FUNCTION_CARD_POWER_RSP, bytes);
	}

	/**
	 * 获取控制卡状态
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public DeviceType getDeviceType() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_GET_DEVICE_TYPE_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_DEVICE_TYPE_RSP);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 19) {
				return null;
			}

			DeviceType mDeviceType = new DeviceType();

			int day = recvBuf[4];
			int month = recvBuf[3] - 1;
			int year = (short) (0xffff & NovaUtils.bytesToShort(recvBuf, 1)) - 1900;
			int second = recvBuf[7];
			int minute = recvBuf[6];
			int hour = recvBuf[5];
			mDeviceType.setDate(new Date(year, month, day, hour, minute, second));

			mDeviceType.setBoxDoorOpen(recvBuf[8] == 1);
			mDeviceType.setScreenPowerOpen(recvBuf[9] == 1);
			int sign = recvBuf[11] == 1 ? 1 : -1;
			mDeviceType.setTemperature(recvBuf[12] * sign);
			mDeviceType.setInputSourceInside(recvBuf[13] == 1);
			mDeviceType.setEnvironmentBrightness(0xff & recvBuf[16]);
			mDeviceType.setBrightnessType(recvBuf[17]);
			mDeviceType.setScreenBrightness(0xff & recvBuf[18]);

			return mDeviceType;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置时间
	 *
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int setTime(Date date) {
		byte[] bytes = new byte[7];
		NovaUtils.shortToBytes(bytes, 0, (short) (0xffff & date.getYear() - 1900));
		bytes[2] = (byte) (0xff & (date.getMonth() + 1));
		bytes[3] = (byte) (0xff & date.getDate());
		bytes[4] = (byte) (0xff & date.getHours());
		bytes[5] = (byte) (0xff & date.getMinutes());
		bytes[6] = (byte) (0xff & date.getSeconds());
		return normalSet(NovaUtils.WHAT_DEVICE_SET_TIME_REQ, NovaUtils.WHAT_DEVICE_SET_TIME_RSP, bytes);

	}

	/**
	 * 重启设备
	 *
	 * @return
	 */
	public int restartDevice() {
		byte[] bytes = new byte[0];

		return normalSet(NovaUtils.WHAT_DEVICE_REBOOT_REQ, NovaUtils.WHAT_DEVICE_REBOOT_RSP, bytes);

	}

	/**
	 * 恢复ip到出厂的192.168.0.220
	 */
	public void resetDeviceIpToDefault() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_DEVICE_RESET_IP_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);
			return;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} finally {
			closeSocket(socket);
		}

	}

	/**
	 * 获取控制卡版本
	 *
	 * @return
	 */
	public String getDeviceVersion() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_QUERY_VERSIONINFO_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_QUERY_VERSIONINFO_RSP);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 2) {
				return new String(recvBuf, 1, recvBuf.length - 1);
			}
			return null;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}

	}

	/**
	 * 设置屏体基本参数
	 *
	 * @return
	 */
	public void setDeviceNetBasicParam(DeviceNetBasicParam mDeviceBasicParam) {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return;
			}
			byte[] bytes = new byte[26];
			NovaUtils.normalIntToBytes(bytes, 2, NovaUtils.ipToInt(mDeviceBasicParam.getDeviceIp()));
			NovaUtils.shortToBytes(bytes, 6, (short) (0xffff & mDeviceBasicParam.getPort()));
			NovaUtils.normalIntToBytes(bytes, 8, NovaUtils.ipToInt(mDeviceBasicParam.getMark()));
			NovaUtils.normalIntToBytes(bytes, 12, NovaUtils.ipToInt(mDeviceBasicParam.getGate()));
			NovaUtils.normalIntToBytes(bytes, 16, NovaUtils.ipToInt(mDeviceBasicParam.getServiceIp()));
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SET_DEVICE_IP_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置设备加密
	 *
	 * @param mDeviceSecretParam
	 * @return
	 */
	public int setDeviceSecretParam(DeviceSecretParam mDeviceSecretParam) {
		int offset = 0;
		byte bytes[] = new byte[TEMP_SIZE];
		bytes[offset++] = mDeviceSecretParam.isDesOpen() ? (byte) 1 : 0;
		bytes[offset++] = mDeviceSecretParam.isMD5Open() ? (byte) 1 : 0;
		String password = mDeviceSecretParam.getPassword();
		try {
			if (password != null && getBytes(password).length > 0 && getBytes(password).length <= 16) {
				System.arraycopy(password.getBytes(), 0, bytes, offset, getBytes(password).length);
				offset += getBytes(password).length;
			} else {
				return ResultCode.PARAM_ERROR.getKey();
			}

			bytes[offset++] = (byte) 0x91;
			bytes[offset++] = 0x21;

			String des = mDeviceSecretParam.getDesPassword();
			if (!mDeviceSecretParam.isDesOpen()) {

			} else if (mDeviceSecretParam.isDesOpen() && des != null && des.getBytes().length == 8) {
				System.arraycopy(des.getBytes(), 0, bytes, offset, 8);
				offset += 8;
			} else {
				return ResultCode.PARAM_ERROR.getKey();
			}
			byte out[] = new byte[offset];
			System.arraycopy(bytes, 0, out, 0, offset);
			int ret = normalSet(NovaUtils.WHAT_SECRET_DES_MD5_REQ, NovaUtils.WHAT_SECRET_DES_MD5_RSP, out);
			if (ret == SUCCESS) {
				mProtocolParser = null;
			}
			return ret;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}
	}

	/**
	 * 升级文件
	 *
	 * @param path 文件路径
	 * @return
	 */
	public int updateFileName(String path) {
		if (path == null) {
			return ResultCode.PARAM_ERROR.getKey();
		}

		try {
			return normalSet(NovaUtils.WHAT_DEVICE_REBOOT_REQ, NovaUtils.WHAT_DEVICE_REBOOT_RSP, getBytes(path));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}

	}

	/**
	 * 获取屏幕尺寸（宽高）
	 *
	 * @return
	 */
	public DeviceSize getDeviceSize() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_WIDTH_HEIGHT_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_SCREEN_WIDTH_HEIGHT_RSP);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 5) {
				return null;
			}

			int width = NovaUtils.bytesToShort(recvBuf, 1);
			int height = NovaUtils.bytesToShort(recvBuf, 3);
			return new DeviceSize(width, height);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置音量0-100
	 *
	 * @param volume 0 -100
	 * @return
	 */
	public int setDeviceVolume(int volume) {
		byte bytes[] = new byte[1];
		bytes[0] = (byte) (0xff & volume);
		return normalSet(NovaUtils.WHAT_SET_VOLUME_PARAM_REQ, NovaUtils.WHAT_SET_VOLUME_PARAM_RSP, bytes);
	}

	/**
	 * 获取设备空间
	 *
	 * @return
	 */
	public DeviceMemorySize getDeviceMemorySize() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_GET_SDCARD_SIZE_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_SDCARD_SIZE_RSP);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 9) {
				return null;
			}

			int totalSize = NovaUtils.bytesToInt(recvBuf, 1);
			int availableSize = NovaUtils.bytesToInt(recvBuf, 5);
			return new DeviceMemorySize(totalSize, availableSize);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置时区
	 *
	 * @param timeZoneParam
	 * @return
	 */
	public int setTimeAndTimeZone(TimeZoneParam timeZoneParam) {
		String outStr = Long.toString(timeZoneParam.getUtcTime()) + "," + timeZoneParam.getTimeZoneId() + ","
				+ timeZoneParam.getGmt();
		try {
			return normalSet(NovaUtils.SET_TIME_AND_ZONE, NovaUtils.SET_TIME_AND_ZONE, getBytes(outStr));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}

	}

	/**
	 * 获取时区
	 *
	 * @return
	 */
	public TimeZoneParam getTimeAndTimeZone() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.GET_TIME_AND_ZONE, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.GET_TIME_AND_ZONE);
			if (recvBuf == null) {
				return null;
			}

			String temp = new String(recvBuf, 1, recvBuf.length - 1);
			String[] listStr = temp.split(",");
			Long utcTimeMillis = Long.valueOf(listStr[0]);
			return new TimeZoneParam(utcTimeMillis, listStr[1], listStr[2]);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置ntp对时参数
	 *
	 * @param deviceNTPParam
	 * @return
	 */
	public int setDeviceNTPParam(DeviceNTPParam deviceNTPParam) {
		if (deviceNTPParam.getAddr() == null) {
			deviceNTPParam.setAddr("");
		}

		byte[] bytes;
		try {
			bytes = new byte[1 + getBytes(deviceNTPParam.getAddr()).length];
			bytes[0] = deviceNTPParam.isOpen() ? (byte) 1 : 0;
			System.arraycopy(getBytes(deviceNTPParam.getAddr()), 0, bytes, 1,
					getBytes(deviceNTPParam.getAddr()).length);
			return normalSet(NovaUtils.SET_NTP_PARAM, NovaUtils.SET_NTP_PARAM, bytes);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}
	}

	/**
	 * 获取ntp对时参数
	 *
	 * @return
	 */
	public DeviceNTPParam getDeviceNTPParam() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.GET_NTP_PARAM, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.GET_NTP_PARAM);
			if (recvBuf == null) {
				return null;
			}

			boolean isOpen = recvBuf[0] == 1 ? true : false;

			return new DeviceNTPParam(isOpen, new String(recvBuf, 1, recvBuf.length - 1, "utf-8"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置屏体关闭温度，高于此温度关屏
	 *
	 * @param value 系统最小值为 70 0 表示不进行关屏处理
	 * @return
	 */
	public int setCloseScreenTemperature(int value) {
		byte[] bytes = new byte[6];
		if (value < 0 || value > 150) {
			return ResultCode.PARAM_ERROR.getKey();
		}
		bytes[5] = (byte) (0xff & value);
		return normalSet(NovaUtils.WHAT_SET_ENVIRONMENT_ALARM_REQ, NovaUtils.WHAT_SET_ENVIRONMENT_ALARM_RSP, bytes);
	}

	/**
	 * 获取 屏体关闭温度
	 *
	 * @return
	 */
	public int getCloseScreenTemperature() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_GET_ENVIRONMENT_ALARM_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_ENVIRONMENT_ALARM_RSP);
			if (recvBuf == null) {
				return ResultCode.SEND_NOT_RECV_ERROR.getKey();
			}

			return recvBuf[6];
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置虚连接播放文件
	 *
	 * @param isOpen     false-不开启 true-开启虚连接检测
	 * @param secondTime 单位秒，多长时间无数据交互开启虚连接
	 * @param id         播放文件id 1 代表play001.lst文件
	 * @return
	 */
	public int setVirtualConnection(boolean isOpen, int secondTime, int id) {
		byte[] bytes = new byte[4];
		bytes[0] = isOpen ? (byte) 1 : 0;

		if (secondTime < 60) {
			secondTime = 60;
		}
		NovaUtils.shortToBytes(bytes, 1, (short) (0xffff & secondTime));
		bytes[3] = (byte) (0xff & id);
		return normalSet(NovaUtils.WHAT_VIRTUAL_CONNECT_PARAM_REQ, NovaUtils.WHAT_VIRTUAL_CONNECT_PARAM_RSP, bytes);
	}

	/**
	 * 发送ftp下载文件地址
	 *
	 * @param url ftp://user:password@192.168.0.108:21/play001.lst
	 * @return
	 */
	public int sendFtpDownloadaddress(String url) {
		if (url == null || url.length() == 0) {
			return ResultCode.PARAM_ERROR.getKey();
		}
		try {
			return normalSet(NovaUtils.WHAT_SET_FTP_DOWNLOAD_REQ, NovaUtils.WHAT_SET_FTP_DOWNLOAD_RSP, getBytes(url));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.PARAM_ERROR.getKey();
		}
	}

	/**
	 * 获取当前播放内容
	 *
	 * @return
	 */
	public DeviceNowPlayItem getNowPlayContent() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_GET_PLAYING_ITEM_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_PLAYING_ITEM_RSP);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 4) {
				boolean isOpen = recvBuf[1] == 1 ? true : false;
				String content = new String(recvBuf, 4, recvBuf.length - 4, "utf-8");
				return new DeviceNowPlayItem(isOpen, recvBuf[3], content);
			}
			return null;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 获取当前播放全部内容
	 *
	 * @return
	 */
	public DeviceNowPlayList getNowPlayAllContent() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_GET_PLAYING_ITEM_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_PLAYING_ITEM_RSP);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 2) {
				String content = new String(recvBuf, 2, recvBuf.length - 2, "utf-8");
				return new DeviceNowPlayList(recvBuf[1], content);
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 获取当前播放截图
	 *
	 * @param filePath 截图文件名
	 * @return
	 */
	public int getDeviceScreenshot(String filePath) {
		File file = new File(filePath);
		Socket socket = null;
		if (!FileUtils.createEmptyFile(file))
			return ResultCode.PARAM_ERROR.getKey();

		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}
			byte[] bytes = new byte[2];
			NovaUtils.shortToBytes(bytes, 0, (short) (0xffff & BIG_SIZE));
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREENSHOT_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			while (true) {
				byte[] inputBuf = new byte[BIG_SIZE * 2];
				byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_SCREENSHOT_RSP);
				FileUtils.writeFileAppend(file, recvBuf, 3, recvBuf.length - 3);
				if (recvBuf.length - 3 < BIG_SIZE) {
					return SUCCESS;
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置定时开屏列表
	 *
	 * @param list 为null 或 0 清除操作 ；新表覆盖旧表
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int setDevicePowerByTimeList(List<DevicePowerByTimeParam> list) {
		byte[] bytes;
		if (list == null || list.size() == 0) {
			bytes = new byte[1];
			bytes[0] = 0;
		} else {
			int itemSize = 14;
			bytes = new byte[1 + list.size() * itemSize];
			bytes[0] = (byte) list.size();
			for (int i = 0; i < list.size(); i++) {
				bytes[1 + i * itemSize] = (byte) (list.get(i).getStart().getDate());
				bytes[2 + i * itemSize] = (byte) (list.get(i).getStart().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 3 + i * itemSize, (short) (list.get(i).getStart().getYear() + 1900));
				bytes[5 + i * itemSize] = (byte) list.get(i).getStart().getSeconds();
				bytes[6 + i * itemSize] = (byte) list.get(i).getStart().getMinutes();
				bytes[7 + i * itemSize] = (byte) list.get(i).getStart().getHours();

				bytes[8 + i * itemSize] = (byte) (list.get(i).getEnd().getDate());
				bytes[9 + i * itemSize] = (byte) (list.get(i).getEnd().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 10 + i * itemSize, (short) (list.get(i).getEnd().getYear() + 1900));
				bytes[12 + i * itemSize] = (byte) list.get(i).getEnd().getSeconds();
				bytes[13 + i * itemSize] = (byte) list.get(i).getEnd().getMinutes();
				bytes[14 + i * itemSize] = (byte) list.get(i).getEnd().getHours();
			}
		}
		return normalSet(NovaUtils.WHAT_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_REQ,
				NovaUtils.WHAT_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_RSP, bytes);
	}

	/**
	 * 获取定时开屏列表
	 */
	@SuppressWarnings("deprecation")
	public List<DevicePowerByTimeParam> getDevicePowerByTimeList() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.GET_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_INFO, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf,
					NovaUtils.GET_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_INFO);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 2) {
				int length = recvBuf[1];
				int itemSize = 14;
				List<DevicePowerByTimeParam> list = new ArrayList<DevicePowerByTimeParam>();
				for (int i = 0; i < length; i++) {
					DevicePowerByTimeParam param = new DevicePowerByTimeParam();
					int day = recvBuf[2 + i * itemSize];
					int month = recvBuf[3 + i * itemSize] - 1;
					int year = NovaUtils.bytesToShort(recvBuf, 4 + i * itemSize) - 1900;
					int second = recvBuf[6 + i * itemSize];
					int minute = recvBuf[7 + i * itemSize];
					int hour = recvBuf[8 + i * itemSize];
					param.setStart(new Date(year, month, day, hour, minute, second));

					day = recvBuf[9 + i * itemSize];
					month = recvBuf[10 + i * itemSize] - 1;
					year = NovaUtils.bytesToShort(recvBuf, 11 + i * itemSize) - 1900;
					second = recvBuf[13 + i * itemSize];
					minute = recvBuf[14 + i * itemSize];
					hour = recvBuf[15 + i * itemSize];
					param.setEnd(new Date(year, month, day, hour, minute, second));
					list.add(param);
				}
				return list;
			}
			return null;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置定时播放列表
	 *
	 * @param list
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int setPlayByTimeList(List<PlayByTimeParam> list) {
		byte[] bytes;
		if (list == null || list.size() == 0) {
			bytes = new byte[1];
			bytes[0] = 0;
		} else {
			int itemSize = 16;
			bytes = new byte[1 + list.size() * itemSize];
			bytes[0] = (byte) list.size();
			for (int i = 0; i < list.size(); i++) {
				bytes[1 + i * itemSize] = (byte) list.get(i).getStart().getDate();
				bytes[2 + i * itemSize] = (byte) (list.get(i).getStart().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 3 + i * itemSize, (short) (list.get(i).getStart().getYear() + 1900));
				bytes[5 + i * itemSize] = (byte) list.get(i).getStart().getSeconds();
				bytes[6 + i * itemSize] = (byte) list.get(i).getStart().getMinutes();
				bytes[7 + i * itemSize] = (byte) list.get(i).getStart().getHours();

				bytes[8 + i * itemSize] = (byte) list.get(i).getEnd().getDate();
				bytes[9 + i * itemSize] = (byte) (list.get(i).getEnd().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 10 + i * itemSize, (short) (list.get(i).getEnd().getYear() + 1900));
				bytes[12 + i * itemSize] = (byte) list.get(i).getEnd().getSeconds();
				bytes[13 + i * itemSize] = (byte) list.get(i).getEnd().getMinutes();
				bytes[14 + i * itemSize] = (byte) list.get(i).getEnd().getHours();

				bytes[15 + i * itemSize] = (byte) list.get(i).getId();
			}
		}
		return normalSet(NovaUtils.WHAT_SET_TIMING_PLAYLIST_PARAM_REQ, NovaUtils.WHAT_SET_TIMING_PLAYLIST_PARAM_RSP,
				bytes);
	}

	/**
	 * 获取定时播放列表
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<PlayByTimeParam> getPlayByTimeList() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.GET_SCREEN_TIMING_PLAYLIST_PARAM_INFO, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.GET_SCREEN_TIMING_PLAYLIST_PARAM_INFO);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 2) {
				int length = recvBuf[1];
				int itemSize = 16;
				List<PlayByTimeParam> list = new ArrayList<PlayByTimeParam>();
				for (int i = 0; i < length; i++) {
					PlayByTimeParam param = new PlayByTimeParam();
					int day = recvBuf[2 + i * itemSize];
					int month = recvBuf[3 + i * itemSize] - 1;
					int year = NovaUtils.bytesToShort(recvBuf, 4 + i * itemSize) - 1900;
					int second = recvBuf[6 + i * itemSize];
					int minute = recvBuf[7 + i * itemSize];
					int hour = recvBuf[8 + i * itemSize];
					param.setStart(new Date(year, month, day, hour, minute, second));

					day = recvBuf[9 + i * itemSize];
					month = recvBuf[10 + i * itemSize] - 1;
					year = NovaUtils.bytesToShort(recvBuf, 11 + i * itemSize) - 1900;
					second = recvBuf[13 + i * itemSize];
					minute = recvBuf[14 + i * itemSize];
					hour = recvBuf[15 + i * itemSize];
					param.setEnd(new Date(year, month, day, hour, minute, second));

					param.setId(recvBuf[16 + i * itemSize]);
					list.add(param);
				}
				return list;
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 设置定时亮度列表
	 *
	 * @param list
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public int setBrightnessByTimeList(List<BrightnessByTimeParam> list) {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			// 设置定时
			byte[] bytes = new byte[2];
			bytes[0] = 3; // 定时
			bytes[1] = 0;
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_BRIGHTNESS_MODE_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}

		byte[] bytes = null;
		if (list == null || list.size() == 0) {
			bytes = new byte[1];
			bytes[0] = 0;
		} else {
			int itemSize = 15;
			bytes = new byte[1 + list.size() * itemSize];
			bytes[0] = (byte) list.size();
			for (int i = 0; i < list.size(); i++) {
				bytes[1 + i * itemSize] = (byte) list.get(i).getStart().getDate();
				bytes[2 + i * itemSize] = (byte) (list.get(i).getStart().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 3 + i * itemSize, (short) (list.get(i).getStart().getYear() + 1900));
				bytes[5 + i * itemSize] = (byte) list.get(i).getStart().getSeconds();
				bytes[6 + i * itemSize] = (byte) list.get(i).getStart().getMinutes();
				bytes[7 + i * itemSize] = (byte) list.get(i).getStart().getHours();

				bytes[8 + i * itemSize] = (byte) list.get(i).getEnd().getDate();
				bytes[9 + i * itemSize] = (byte) (list.get(i).getEnd().getMonth() + 1);
				NovaUtils.shortToBytes(bytes, 10 + i * itemSize, (short) (list.get(i).getEnd().getYear() + 1900));
				bytes[12 + i * itemSize] = (byte) list.get(i).getEnd().getSeconds();
				bytes[13 + i * itemSize] = (byte) list.get(i).getEnd().getMinutes();
				bytes[14 + i * itemSize] = (byte) list.get(i).getEnd().getHours();

				bytes[15 + i * itemSize] = (byte) list.get(i).getBrightness();
			}
		}
		return normalSet(NovaUtils.WHAT_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_REQ,
				NovaUtils.WHAT_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_RSP, bytes);

	}

	/**
	 * 获取定时亮度列表
	 *
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<BrightnessByTimeParam> getBrightnessByTimeList() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.GET_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_INFO, bytes,
					bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf,
					NovaUtils.GET_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_INFO);

			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length >= 2) {
				int itemSize = 15;
				int length = recvBuf[1];
				List<BrightnessByTimeParam> list = new ArrayList<BrightnessByTimeParam>();
				for (int i = 0; i < length; i++) {
					BrightnessByTimeParam param = new BrightnessByTimeParam();
					int day = recvBuf[2 + i * itemSize];
					int month = recvBuf[3 + i * itemSize] - 1;
					int year = NovaUtils.bytesToShort(recvBuf, 4 + i * itemSize) - 1900;
					int second = recvBuf[6 + i * itemSize];
					int minute = recvBuf[7 + i * itemSize];
					int hour = recvBuf[8 + i * itemSize];
					param.setStart(new Date(year, month, day, hour, minute, second));

					day = recvBuf[9 + i * itemSize];
					month = recvBuf[10 + i * itemSize] - 1;
					year = NovaUtils.bytesToShort(recvBuf, 11 + i * itemSize) - 1900;
					second = recvBuf[13 + i * itemSize];
					minute = recvBuf[14 + i * itemSize];
					hour = recvBuf[15 + i * itemSize];
					param.setEnd(new Date(year, month, day, hour, minute, second));

					param.setBrightness(recvBuf[16 + i * itemSize]);
					list.add(param);
				}
				return list;
			}
			return null;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 点检需要配合lct点检功能使用，不能单独使用
	 *
	 * @return 大于0 为点检坏块总数。
	 */
	public int spotCheckBadBlockInit() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_POINT_DETECT_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_SCREEN_POINT_DETECT_RSP);
			if (recvBuf == null) {
				return ResultCode.SEND_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length != 4 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_RECV_ERROR.getKey();
			}

			return (0xffff & NovaUtils.bytesToShort(recvBuf, 2));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 获取坏块信息
	 *
	 * @return
	 */
	public String getSpotCheckBadBlockInfo() {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return null;
			}
			byte[] bytes = new byte[0];
			byte[] outbuf = makeSendPacket(NovaUtils.WHAT_SCREEN_POINT_DETECT_REQ, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[BIG_SIZE];
			// 消息回传判断
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_SCREEN_POINT_DETECT_RSP);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 4 && recvBuf[1] != SUCCESS) {
				return null;
			}

			outbuf = makeSendPacket(NovaUtils.WHAT_GET_ERROR_POINT_REQ1, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			int recvBufSize = 0;
			// 消息回传判断
			recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_ERROR_POINT_RSP1);
			if (recvBuf == null) {
				return null;
			} else if (recvBuf.length != 4 && recvBuf[1] != SUCCESS) {
				return null;
			}
			recvBufSize = 0xffff & NovaUtils.bytesToShort(recvBuf, 2);
			String out = "";
			// 消息回传判断
			while (true) {
				recvBuf = recv(socket.getInputStream(), inputBuf, NovaUtils.WHAT_GET_ERROR_POINT_RSP2);
				if (recvBuf == null) {
					return null;
				}
				out += new String(recvBuf, 3, recvBuf.length - 3);
				if (recvBuf.length < recvBufSize) {
					return out;
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 开关继电器状态
	 *
	 * @param power1
	 * @param power2
	 * @param power3
	 * @param power4
	 * @return
	 */
	public int setRelayPowers(boolean power1, boolean power2, boolean power3, boolean power4) {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			byte[] bytes = new byte[4];
			bytes[0] = power1 ? (byte) 1 : 0;
			bytes[1] = power2 ? (byte) 1 : 0;
			bytes[2] = power3 ? (byte) 1 : 0;
			bytes[3] = power4 ? (byte) 1 : 0;
			byte[] outbuf = makeSendPacket(NovaUtils.SET_RELAY, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 发送文件名回传.
			byte recvBuf[] = recv(socket.getInputStream(), inputBuf, NovaUtils.SET_RELAY);
			if (recvBuf == null) {
				return ResultCode.SEND_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_RECV_ERROR.getKey();
			}

			return SUCCESS;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}
	}

	/**
	 * 当一条发送，需要获取指定消息。
	 *
	 * @param stream
	 * @param inputBuf
	 * @param what
	 * @return
	 */
	private byte[] recv(InputStream stream, byte[] inputBuf, byte what) {
		int failCount = 0;
		if (mProtocolParser == null) {
			if (mDesStr != null) {
				mProtocolParser = new ProtocolParser(mDesStr);
			} else {
				mProtocolParser = new ProtocolParser();
			}
		}

		while (true) {
			try {
				byte[] recvBuf;
				if (mProtocolParser.remainBuffer != null && !mProtocolParser.isNeedReload) {
					recvBuf = mProtocolParser.recvProc(inputBuf, 0, what);
				} else {
					int len = stream.read(inputBuf, 0, inputBuf.length);
					recvBuf = mProtocolParser.recvProc(inputBuf, len, what);
				}
				if (recvBuf != null) {
					return recvBuf;
				}
			} catch (Exception e) {
				return null;
			}

			failCount++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (failCount >= MAX_FAIL_COUNT) {
				return null;
			}
		}

	}

	/**
	 * 通用设置
	 *
	 * @param sendWhat
	 * @param recvWhat
	 * @param bytes
	 * @return
	 */
	private int normalSet(byte sendWhat, byte recvWhat, byte[] bytes) {
		Socket socket = null;
		try {
			socket = getSocket();
			if (socket == null) {
				return ResultCode.SOCKET_ERROR.getKey();
			}

			byte[] outbuf = makeSendPacket(sendWhat, bytes, bytes.length);
			socket.getOutputStream().write(outbuf);

			byte[] inputBuf = new byte[TEMP_SIZE];
			// 消息回传判断.
			byte[] recvBuf = recv(socket.getInputStream(), inputBuf, recvWhat);
			if (recvBuf == null) {
				return ResultCode.SEND_NOT_RECV_ERROR.getKey();
			} else if (recvBuf.length >= 2 && recvBuf[1] != SUCCESS) {
				return ResultCode.SEND_RECV_ERROR.getKey();
			}

			return SUCCESS;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.UNKNOWN_HOST.getKey();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultCode.IO_ERROR.getKey();
		} finally {
			closeSocket(socket);
		}

	}
}
