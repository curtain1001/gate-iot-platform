package net.pingfang.device.novaled.core;

import net.pingfang.device.novaled.utils.DesUtil;
import net.pingfang.device.novaled.utils.NovaUtils;

public class ProtocolParser {

	private static final byte PACKET_START_FLAG = (byte) 0xAA;
	private static final byte PACKET_END_FLAG = (byte) 0xCC;
	private static final byte PACKET_TRANSFER_FLAG = (byte) 0xEE;

	private static final int PACKET_MIN_SIZE = 7;
	private static final int PACKET_WITHOUT_WHAT_AND_DATA = 6;
	private static final int PACKET_WHAT_OFFSET = 3;
	private static final int PACKET_DATA_OFFSET = 4;
	private static final byte DES_ADD_SIZE = 7;
	private static final int PACKET_WITHOUT_DATA_SIZE = 7;

	private static final int DEFLAUT_PARK_BLOCK_SIZE = 4096;
	private static final int END_CRC_SIZE = 2;
	private static final int PACKET_CRC_SIZE = 2;
	private static final int NO_START_RCV = -1;
	private static final int START_RCV = 0;
	private static final int START_RCV_END = 1;

	private static final byte WHAT_SECRET_DES_MD5_REQ = 0x70;

	private int pkgType = NO_START_RCV; // -1为 开始 0 开始 1结束

	private byte[] mTempBuffer = null;
	private int maxBufferSize; // 超过就补4096
	private int mBufferLen;
	private int endNeed = END_CRC_SIZE;
	private String mDesSecret = null;
	public byte[] remainBuffer = null;
	public boolean isNeedReload = false;

	public ProtocolParser() {

	}

	public ProtocolParser(String mDesSecret) {
		this.mDesSecret = mDesSecret;
	}

	/**
	 * 用于拆包，从buffer中解析中含有what的相关码流
	 *
	 * @param buffer
	 * @param count
	 * @param what
	 * @return
	 */
	public byte[] recvProc(byte[] buffer, int count, byte what) {
		// 进入后先初始化
		isNeedReload = false;
		if (remainBuffer != null) {
			if (count == 0) {
				buffer = remainBuffer;
				count += remainBuffer.length;
			} else {
				byte[] copyedBuffer = new byte[count + remainBuffer.length];
				System.arraycopy(remainBuffer, 0, copyedBuffer, 0, remainBuffer.length);
				System.arraycopy(buffer, 0, copyedBuffer, remainBuffer.length, count);
				buffer = copyedBuffer;
				count += remainBuffer.length;
			}
		}

		pkgType = START_RCV;
		mTempBuffer = new byte[DEFLAUT_PARK_BLOCK_SIZE];
		maxBufferSize = DEFLAUT_PARK_BLOCK_SIZE;
		mBufferLen = 0;
		{
			int copyed = 0;
			while (copyed != count) {
				switch (pkgType) {
				case NO_START_RCV:
					if (buffer[copyed] != PACKET_START_FLAG) {
						// LOGGER.warn("等待接收通信数据包头！");
					} else {
						pkgType = START_RCV;
						mTempBuffer = new byte[DEFLAUT_PARK_BLOCK_SIZE];
						maxBufferSize = DEFLAUT_PARK_BLOCK_SIZE;
						mBufferLen = 0;
						mTempBuffer[mBufferLen++] = buffer[copyed];
					}
					break;
				case START_RCV:
					// 空间不够补充
					if (mBufferLen == maxBufferSize) {
						maxBufferSize += DEFLAUT_PARK_BLOCK_SIZE;
						byte[] temp = new byte[maxBufferSize];
						System.arraycopy(mTempBuffer, 0, temp, 0, mTempBuffer.length);
						mTempBuffer = temp;
					}
					// set buffer
					mTempBuffer[mBufferLen++] = buffer[copyed];

					// endtype
					if (buffer[copyed] == PACKET_END_FLAG) {
						pkgType = START_RCV_END;
						endNeed = END_CRC_SIZE;
					}
					break;
				case START_RCV_END: {
					// 空间不够补充
					if (mBufferLen == maxBufferSize) {
						maxBufferSize += DEFLAUT_PARK_BLOCK_SIZE;
						byte[] temp = new byte[maxBufferSize];
						System.arraycopy(mTempBuffer, 0, temp, 0, mTempBuffer.length);
						mTempBuffer = temp;
					}
					mTempBuffer[mBufferLen++] = buffer[copyed];
					endNeed--;
					if (endNeed == 0) {
						pkgType = NO_START_RCV;
						byte ret[] = null;
						ret = recvPackage(mTempBuffer, mBufferLen, what);
						if (ret != null) {
							if (count == copyed + 1) {
								remainBuffer = null;
							} else {
								remainBuffer = new byte[count - copyed - 1];
								System.arraycopy(buffer, copyed + 1, remainBuffer, 0, remainBuffer.length);
							}
							return ret;
						}
					}
				}
				default:
					;
				}
				copyed++;
			}
			isNeedReload = true;
			return null;
		}
	}

	private byte[] recvPackage(byte[] buffer, int count, byte what) {

		int tempSize = 0;
		int copySize = 0;

		// 起始符，结束符，长度校验
		if (count < PACKET_MIN_SIZE) {
			// LOGGER.warn("收到的数据长度小于通信数据包最小长度！");
			return null;
		}

		// 不是需要的what
		if (buffer[3] != what) {
			return null;
		}

		// CRC校验
		short mCrc = NovaUtils.pubCalcCRC(buffer, count - PACKET_CRC_SIZE);
		short mCrcCode = NovaUtils.bytesToShort(buffer, count - PACKET_CRC_SIZE);
		if (mCrc != mCrcCode) {
			// LOGGER.warn("CRC校验失败！");
			return null;
		}

		// decode 0xee
		byte[] mRecvBuffer = new byte[count];
		while (tempSize < count - 2) {
			if (buffer[tempSize] != PACKET_TRANSFER_FLAG) {
				mRecvBuffer[copySize] = buffer[tempSize];

			} else {
				tempSize++;
				mRecvBuffer[copySize] = buffer[tempSize];
				if (mRecvBuffer[copySize] == 0x0a) {
					mRecvBuffer[copySize] = (byte) 0xAA;
				} else if (mRecvBuffer[copySize] == 0x0c) {
					mRecvBuffer[copySize] = (byte) 0xcc;
				} else if ((mRecvBuffer[copySize] == 0x0e)) {
					mRecvBuffer[copySize] = (byte) 0xee;
				} else {
					// LOGGER.warn("数据转义0xEE错误！");
					return null;
				}
			}
			tempSize++;
			copySize++;
		}

		// crc 2位
		mRecvBuffer[copySize++] = buffer[tempSize++];
		mRecvBuffer[copySize++] = buffer[tempSize++];

		// decode DES 设置加密属性不加密
		if (mDesSecret != null && copySize > PACKET_MIN_SIZE && buffer[3] != WHAT_SECRET_DES_MD5_REQ) {
			try {
				byte[] tempOutData = DesUtil.decrypt(mRecvBuffer, PACKET_DATA_OFFSET, copySize - PACKET_MIN_SIZE,
						mDesSecret.getBytes());
				System.arraycopy(tempOutData, 0, mRecvBuffer, PACKET_DATA_OFFSET, tempOutData.length);
				copySize = tempOutData.length + PACKET_WITHOUT_DATA_SIZE;

			} catch (Exception e) {
				return null;
			}
		}

		// 外传结构
		int outSize = copySize - PACKET_WITHOUT_WHAT_AND_DATA;
		byte[] mBuffer = new byte[outSize];
		System.arraycopy(mRecvBuffer, PACKET_WHAT_OFFSET, mBuffer, 0, outSize);
		return mBuffer;
	}

}
