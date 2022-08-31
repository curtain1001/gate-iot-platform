package net.pingfang.device.novaled.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DesUtil
 *
 * @author Administrator
 *
 */
public class DesUtil {
	public static byte[] encrypt(byte[] message, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message);
	}

	public static byte[] encrypt(byte[] message, int offset, int length, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		DESKeySpec desKeySpec = new DESKeySpec(key);

		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message, offset, length);
	}

	public static byte[] decrypt(byte[] message, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key);

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message);
	}

	public static byte[] decrypt(byte[] message, int offset, int length, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key);

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		return cipher.doFinal(message, offset, length);
	}

}
