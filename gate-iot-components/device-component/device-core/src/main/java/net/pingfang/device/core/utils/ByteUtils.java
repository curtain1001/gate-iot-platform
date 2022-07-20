package net.pingfang.device.core.utils;

/**
 * @author 王超
 * @date 2021-10-28 18:04
 */
public class ByteUtils {
	/**
	 * 将byte转为16进制
	 *
	 * @param bytes
	 * @return
	 */
	public static String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

	/**
	 * 将16进制字符串转换为字节数组
	 *
	 * @param str 16进制字符串
	 * @return byte[]
	 */
	public static byte[] string2ByteArr(String str) {
		byte[] bytes;
		bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	/**
	 * @Title: intToBytes @Description: int类型转化成byte数组 @param @param
	 *         value @param @return 参数 @return byte[] 返回类型 @throws
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	/*
	 * byte[]合并
	 */
	public static byte[] byteMergerAll(byte[]... values) {
		int length_byte = 0;
		for (int i = 0; i < values.length; i++) {
			length_byte += values[i].length;
		}
		byte[] all_byte = new byte[length_byte];
		int countLength = 0;
		for (int i = 0; i < values.length; i++) {
			byte[] b = values[i];
			System.arraycopy(b, 0, all_byte, countLength, b.length);
			countLength += b.length;
		}
		return all_byte;
	}

	/**
	 * function:2 hex byte[] to int,use pack
	 *
	 * @param len
	 * @return 2 hex byte[]
	 */
	public static byte[] convertLen2HexByte(int len) {
		byte[] lenByte = new byte[2];
		String hexLen = Integer.toHexString(len);
		// 奇数前补0
		hexLen = (hexLen.length() & 1) == 1 ? "0" + hexLen : hexLen;
		lenByte = convertHexStrToByteArray(hexLen);
		return lenByte;
	}

	/**
	 * 将十六进制字符串转换成字节数组
	 *
	 * @param srcHex 十六进制字符串
	 * @return 字节数组
	 */
	public static byte[] convertHexStrToByteArray(String srcHex) {
		byte[] retByteArray = new byte[srcHex.length() / 2];
		byte tmpByte;
		String sDecode;
		for (int i = 0; i < srcHex.length() / 2; i++) {
			sDecode = "0x" + srcHex.substring(2 * i, 2 * i + 2);
			tmpByte = Integer.decode(sDecode).byteValue();
			retByteArray[i] = tmpByte;
		}
		return retByteArray;
	}

	public static String bytesToHexString(byte[] bArr) {
		StringBuffer sb = new StringBuffer(bArr.length);
		String sTmp;

		for (int i = 0; i < bArr.length; i++) {
			sTmp = Integer.toHexString(0xFF & bArr[i]);
			if (sTmp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTmp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * hex字符串转byte数组
	 *
	 * @param inHex 待转换的Hex字符串
	 * @return 转换后的byte数组结果
	 */
	public static byte[] hexToByteArray(String inHex) {
		int hexlen = inHex.length();
		byte[] result;
		if (hexlen % 2 == 1) {
			// 奇数
			hexlen++;
			result = new byte[(hexlen / 2)];
			inHex = "0" + inHex;
		} else {
			// 偶数
			result = new byte[(hexlen / 2)];
		}
		int j = 0;
		for (int i = 0; i < hexlen; i += 2) {
			result[j] = (byte) Integer.parseInt(inHex.substring(i, i + 2), 16);
			j++;
		}
		return result;
	}

}
