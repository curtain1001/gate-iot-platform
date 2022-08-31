package net.pingfang.device.novaled.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	/**
	 * add by wangyang
	 *
	 * @param file
	 * @return
	 */
	public static byte[] readFile(File file) {
		// 需要读取的文件，参数是文件的路径名加文件名
		FileInputStream fis = null;
		ByteArrayOutputStream outputStream = null;
		if (file.isFile()) {
			// 以字节流方法读取文件
			try {
				fis = new FileInputStream(file);
				// 设置一个，每次 装载信息的容器
				byte[] buffer = new byte[1024];
				outputStream = new ByteArrayOutputStream();
				// 开始读取数据
				int len = 0;// 每次读取到的数据的长度
				while ((len = fis.read(buffer)) != -1) {// len值为-1时，表示没有数据了
					// append方法往sb对象里面添加数据
					outputStream.write(buffer, 0, len);
				}
				// 输出字符串
				return outputStream.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("文件不存在！");
		}
		return null;
	}

	public static void writeFileAppend(File file, byte buffer[], int off, int len) throws IOException {
		FileOutputStream fos = new FileOutputStream(file, true);
		fos.write(buffer, off, len);
		fos.flush();
		fos.close();
	}

	/**
	 * Create a new empty file according the file path
	 *
	 * @param file file path
	 * @return true, create empty file successfully, otherwise false
	 */
	public static boolean createEmptyFile(File file) {
		boolean ret = false;
		file.getParentFile().mkdirs();
		try {
			if (file.exists()) {
				file.delete();
			}
			ret = file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
