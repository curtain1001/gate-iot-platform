package net.pingfang.device.novaled.utils;

/**
 * NovaUtils
 *
 * @author Administrator
 *
 */
public class NovaUtils {
	private static final int PACKET_WITHOUT_DATA_SIZE = 7;
	private static final int PACKET_START_OFFSET = 0;
	private static final byte PACKET_TRANSFER_FLAG = (byte) 0xEE;
	private static final byte PACKET_START_FLAG = (byte) 0xAA;
	private static final byte PACKET_END_FLAG = (byte) 0xCC;

	private static final byte PACKET_DEVICEID_ARG1 = (byte) 0xff;
	private static final byte PACKET_DEVICEID_ARG2 = (byte) 0xff;
	private static final byte DES_ADD_SIZE = 7;

	public static byte WHAT_HEART_BEAT = 0x00;
	public static byte WHAT_GET_DEVICE_TYPE_REQ = 0x01;
	public static byte WHAT_GET_DEVICE_TYPE_RSP = 0x02;
	public static byte WHAT_SCREEN_POWER_REQ = 0x05;
	public static byte WHAT_SCREEN_POWER_RSP = 0x06;

	public static byte WHAT_SCREEN_BRIGHTNESS_MODE_REQ = 0x07;
	public static byte WHAT_SCREEN_BRIGHTNESS_MODE_RSP = 0x08;
	public static byte WHAT_DEVICE_SET_TIME_REQ = 0x09;
	public static byte WHAT_DEVICE_SET_TIME_RSP = 0x0a;
	public static byte WHAT_SCREEN_POINT_DETECT_REQ = 0x0B;
	public static byte WHAT_SCREEN_POINT_DETECT_RSP = 0x0C;
	public static byte WHAT_DEVICE_REBOOT_REQ = 0x0d;
	public static byte WHAT_DEVICE_REBOOT_RSP = 0x0e;
	public static byte WHAT_FILENAME_REQ = 0x11;
	public static byte WHAT_FILENAME_RSP = 0x12;
	public static byte WHAT_FILESEND_REQ = 0x13;
	public static byte WHAT_FILESEND_RSP = 0x14;

	public static byte WHAT_SET_ENVIRONMENT_ALARM_REQ = 0x15;
	public static byte WHAT_SET_ENVIRONMENT_ALARM_RSP = 0x16;

	public static byte WHAT_SCREEN_BRIGHTNESS_SET_AUTO_MODE_PARAM_REQ = 0x17;
	public static byte WHAT_SCREEN_BRIGHTNESS_SET_AUTO_MODE_PARAM_RSP = 0x18;

	public static byte WHAT_SET_DEVICE_IP_REQ = 0x19;
	public static byte WHAT_SET_DEVICE_IP_RSP = 0x1A;

	public static byte WHAT_PLAYBYLIST_REQ = 0x1B;
	public static byte WHAT_PLAYBYLIST_RSP = 0x1C;

	public static byte WHAT_DEVICE_RESET_IP_REQ = 0x21;
	public static byte WHAT_DEVICE_RESET_IP_RSP = 0x22;

	public static byte WHAT_QUERY_VERSIONINFO_REQ = 0x23;
	public static byte WHAT_QUERY_VERSIONINFO_RSP = 0x24;

	public static byte WHAT_SCREEN_MY_BOARD_POWER_AND_POWER_REQ = 0x25;
	public static byte WHAT_SCREEN_MY_BOARD_POWER_AND_POWER_RSP = 0x26;

	public static byte WHAT_GET_ENVIRONMENT_ALARM_REQ = 0x29;
	public static byte WHAT_GET_ENVIRONMENT_ALARM_RSP = 0x2A;

	public static byte WHAT_SCREEN_BRIGHTNESS_GET_AUTO_MODE_PARAM_REQ = 0x2B;
	public static byte WHAT_SCREEN_BRIGHTNESS_GET_AUTO_MODE_PARAM_RSP = 0x2C;

	public static byte WHAT_GET_PLAYING_ITEM_REQ = 0x2D;
	public static byte WHAT_GET_PLAYING_ITEM_RSP = 0x2E;

	public static byte WHAT_GET_ERROR_POINT_REQ1 = 0x36;
	public static byte WHAT_GET_ERROR_POINT_RSP1 = 0x37;
	public static byte WHAT_GET_ERROR_POINT_RSP2 = 0x38;
	public static byte WHAT_GET_ERROR_POINT_REQ2 = 0x39;

	public static byte WHAT_SET_TIMING_PLAYLIST_PARAM_REQ = 0x41;
	public static byte WHAT_SET_TIMING_PLAYLIST_PARAM_RSP = 0x42;

	public static byte WHAT_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_REQ = 0x43;
	public static byte WHAT_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_RSP = 0x44;

	public static byte WHAT_SET_VOLUME_PARAM_REQ = 0x45;
	public static byte WHAT_SET_VOLUME_PARAM_RSP = 0x46;

	public static byte WHAT_GET_SDCARD_SIZE_REQ = 0x47;
	public static byte WHAT_GET_SDCARD_SIZE_RSP = 0x48;

	public static byte WHAT_SET_FTP_DOWNLOAD_REQ = 0x49;
	public static byte WHAT_SET_FTP_DOWNLOAD_RSP = 0x50;

	public static byte GET_SCREEN_BRIGHTNESS_SET_TIMING_MODE_PARAM_INFO = (byte) 0x51;
	public static byte GET_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_INFO = (byte) 0x52;
	public static byte GET_SCREEN_TIMING_PLAYLIST_PARAM_INFO = (byte) 0x53;

	public static byte WHAT_SECRET_DES_MD5_REQ = 0x70;
	public static byte WHAT_SECRET_DES_MD5_RSP = 0x71;

	public static byte WHAT_DEVICE_NAME_REQ = 0x7E;
	public static byte WHAT_DEVICE_NAME_RSP = 0x7F;

	public static byte WHAT_CLEAN_PLAYLIST_REQ = 0x7C;
	public static byte WHAT_CLEAN_PLAYLIST_RSP = 0x7D;

	public static byte WHAT_SCREENSHOT_REQ = (byte) 0x80;
	public static byte WHAT_SCREENSHOT_RSP = (byte) 0x81;

	public static byte WHAT_SCREEN_WIDTH_HEIGHT_REQ = (byte) 0x82;
	public static byte WHAT_SCREEN_WIDTH_HEIGHT_RSP = (byte) 0x83;

	public static byte WHAT_SCREEN_MY_BOARD_POWER_REQ = (byte) 0x84;
	public static byte WHAT_SCREEN_MY_BOARD_POWER_RSP = (byte) 0x85;

	public static byte WHAT_FUNCTION_CARD_POWER_REQ = (byte) 0x86;
	public static byte WHAT_FUNCTION_CARD_POWER_RSP = (byte) 0x87;

	public static byte WHAT_REGION_UPDATE_REQ = (byte) 0x88;
	public static byte WHAT_REGION_UPDATE_RSP = (byte) 0x89;

	public static byte WHAT_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_REQ = (byte) 0x8A;
	public static byte WHAT_SCREEN_OPEN_CLOSE_SET_TIMING_PARAM_RSP = (byte) 0x8B;

	public static byte WHAT_SYSTEM_UPDATE_REQ = (byte) 0x90;
	public static byte WHAT_SYSTEM_UPDATE_RSP = (byte) 0x91;

	public static byte SET_TIME_AND_ZONE = (byte) 0x95;
	public static byte GET_TIME_AND_ZONE = (byte) 0x96;
	public static byte SET_NTP_PARAM = (byte) 0x97;
	public static byte GET_NTP_PARAM = (byte) 0x98;

	public static byte SET_RELAY = (byte) 0x9e;

	public static byte WHAT_VIRTUAL_CONNECT_PARAM_REQ = (byte) 0xF3;
	public static byte WHAT_VIRTUAL_CONNECT_PARAM_RSP = (byte) 0xF4;

	public static byte WHAT_FILESEND_OVER_RSP = (byte) 0xF9;

	private static final int crcTable[] = { 0x0000, 0x1189, 0x2312, 0x329b, 0x4624, 0x57ad, 0x6536, 0x74bf, 0x8c48,
			0x9dc1, 0xaf5a, 0xbed3, 0xca6c, 0xdbe5, 0xe97e, 0xf8f7, 0x1081, 0x0108, 0x3393, 0x221a, 0x56a5, 0x472c,
			0x75b7, 0x643e, 0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed, 0xcb64, 0xf9ff, 0xe876, 0x2102, 0x308b, 0x0210,
			0x1399, 0x6726, 0x76af, 0x4434, 0x55bd, 0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e, 0xfae7, 0xc87c, 0xd9f5,
			0x3183, 0x200a, 0x1291, 0x0318, 0x77a7, 0x662e, 0x54b5, 0x453c, 0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef,
			0xea66, 0xd8fd, 0xc974, 0x4204, 0x538d, 0x6116, 0x709f, 0x0420, 0x15a9, 0x2732, 0x36bb, 0xce4c, 0xdfc5,
			0xed5e, 0xfcd7, 0x8868, 0x99e1, 0xab7a, 0xbaf3, 0x5285, 0x430c, 0x7197, 0x601e, 0x14a1, 0x0528, 0x37b3,
			0x263a, 0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9, 0x8960, 0xbbfb, 0xaa72, 0x6306, 0x728f, 0x4014, 0x519d,
			0x2522, 0x34ab, 0x0630, 0x17b9, 0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a, 0xb8e3, 0x8a78, 0x9bf1, 0x7387,
			0x620e, 0x5095, 0x411c, 0x35a3, 0x242a, 0x16b1, 0x0738, 0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb, 0xa862,
			0x9af9, 0x8b70, 0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c, 0xd3a5, 0xe13e, 0xf0b7, 0x0840, 0x19c, 0x2b52,
			0x3adb, 0x4e64, 0x5fed, 0x6d76, 0x7cff, 0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad, 0xc324, 0xf1bf, 0xe036,
			0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5, 0x4f6c, 0x7df7, 0x6c7e, 0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e,
			0xf2a7, 0xc03c, 0xd1b5, 0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66, 0x7eef, 0x4c74, 0x5dfd, 0xb58b, 0xa402,
			0x9699, 0x8710, 0xf3af, 0xe226, 0xd0bd, 0xc134, 0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7, 0x6e6e, 0x5cf5,
			0x4d7c, 0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028, 0x91a1, 0xa33a, 0xb2b3, 0x4a44, 0x5bcd, 0x6956, 0x78df,
			0x0c60, 0x1de9, 0x2f72, 0x3efb, 0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9, 0x8120, 0xb3bb, 0xa232, 0x5ac5,
			0x4b4c, 0x79d7, 0x685e, 0x1ce1, 0x0d68, 0x3ff3, 0x2e7a, 0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a, 0xb0a3,
			0x8238, 0x93b1, 0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62, 0x3ceb, 0x0e70, 0x1ff9, 0xf78f, 0xe606, 0xd49d,
			0xc514, 0xb1ab, 0xa022, 0x92b9, 0x8330, 0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3, 0x2c6a, 0x1ef1, 0x0f78 };

	/**
	 * Calculate CRC
	 *
	 * @param bytes
	 * @param len
	 * @return
	 */
	public static short pubCalcCRC(byte[] bytes, int len) {
		int crc = 0xffff;
		for (int i = 0; i < len; ++i) {
			int index = (crc ^ bytes[i]) & 0xff;
			crc = (crc >> 8) ^ crcTable[index];
		}
		return (short) (crc & 0xffff);
	}

	/**
	 * convert the bytes to short
	 *
	 * @param buffer
	 * @param offset
	 * @return
	 */
	static public short bytesToShort(byte[] buffer, int offset) {
		short value = (short) ((0xff & (short) buffer[offset]) | (short) ((0xff & (short) buffer[offset + 1]) << 8));
		return value;
	}

	/**
	 * convert the short to bytes
	 *
	 * @param buffer buffer hold result.
	 * @param offset start store offset.
	 * @param value
	 */
	public static void shortToBytes(byte[] buffer, int offset, short value) {
		buffer[offset] = (byte) value;
		buffer[offset + 1] = (byte) (value >> 8);
	}

	/**
	 * convert the bytes to int.
	 *
	 * @param buffer
	 * @param offset
	 * @return
	 */
	static public int bytesToInt(byte[] buffer, int offset) {
		int value = 0;
		value = (int) ((0xff & (int) buffer[offset]) | (int) ((0xff & (int) buffer[offset + 1]) << 8)
				| (int) ((0xff & (int) buffer[offset + 2]) << 16) | (int) ((0xff & (int) buffer[offset + 3]) << 24));
		return value;
	}

	/**
	 * convert the int to bytes.
	 *
	 * @param buffer
	 * @param offset
	 * @param value
	 */
	static public void intToBytes(byte[] buffer, int offset, int value) {
		buffer[offset] = (byte) value;
		buffer[offset + 1] = (byte) (value >> 8);
		buffer[offset + 2] = (byte) (value >> 16);
		buffer[offset + 3] = (byte) (value >> 24);
	}

	static public void normalIntToBytes(byte[] buffer, int offset, int value) {
		buffer[offset + 3] = (byte) (value & 0xff);
		buffer[offset + 2] = (byte) (value >> 8 & 0xff);
		buffer[offset + 1] = (byte) (value >> 16 & 0xff);
		buffer[offset] = (byte) (value >> 24 & 0xff);

	}

	/**
	 * encode data and copy to dst .
	 *
	 * @param dst
	 * @param offset
	 * @param src
	 * @param bufsize
	 * @return int
	 */
	public static int encodeToBytes(byte[] dst, int offset, final byte[] src, int bufsize) {
		for (int i = 0; i < bufsize; i++) {

			byte temp = src[i];
			if (temp == PACKET_TRANSFER_FLAG || temp == PACKET_START_FLAG || temp == PACKET_END_FLAG) {
				dst[offset++] = PACKET_TRANSFER_FLAG;
				dst[offset++] = (byte) (temp & (0x0f));
			} else {
				dst[offset++] = temp;
			}
		}
		return offset;
	}

	/**
	 * make the send packet according to the traffic.
	 * 增加两倍长度+PACKET_WITHOUT_DATA_SIZE+DES_ADD_SIZE。一定大于转码后以及DES加密后长度 牺牲空间节省时间
	 *
	 * @return byte[]
	 */
	public static byte[] makeSendPacket(byte what, byte[] data, int dataSize) {
		return makeSendPacket(what, data, dataSize, false, null);
	}

	/**
	 * make the send packet according to the traffic.
	 * 增加两倍长度+PACKET_WITHOUT_DATA_SIZE+DES_ADD_SIZE。一定大于转码后以及DES加密后长度 牺牲空间节省时间
	 *
	 * @return byte[]
	 */
	public static byte[] makeSendPacket(byte what, byte[] data, int dataSize, boolean bEncodeDES, byte[] desKey) {
		byte[] sendBuffer = new byte[dataSize * 2 + PACKET_WITHOUT_DATA_SIZE + DES_ADD_SIZE];

		int offset = PACKET_START_OFFSET;
		sendBuffer[offset++] = PACKET_START_FLAG;
		sendBuffer[offset++] = PACKET_DEVICEID_ARG1;
		sendBuffer[offset++] = PACKET_DEVICEID_ARG2;
		sendBuffer[offset++] = what;

		if (bEncodeDES && dataSize != 0 && desKey != null && what != WHAT_SECRET_DES_MD5_RSP) {
			try {
				byte[] tempdata = DesUtil.encrypt(data, 0, dataSize, desKey);
				data = tempdata;
				dataSize = tempdata.length;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		offset = encodeToBytes(sendBuffer, offset, data, dataSize);
		sendBuffer[offset++] = PACKET_END_FLAG;

		short mCrc = pubCalcCRC(sendBuffer, offset);
		shortToBytes(sendBuffer, offset, mCrc);
		offset += 2;

		byte[] outBuffer = new byte[offset];
		System.arraycopy(sendBuffer, 0, outBuffer, 0, offset);
		return outBuffer;
	}

	public static int ipToInt(String ipStr) {
		if (ipStr == null || ipStr.length() == 0)
			return 0;

		String[] ip = ipStr.split("\\.");
		return (Integer.parseInt(ip[0]) << 24) + (Integer.parseInt(ip[1]) << 16) + (Integer.parseInt(ip[2]) << 8)
				+ Integer.parseInt(ip[3]);
	}
}
