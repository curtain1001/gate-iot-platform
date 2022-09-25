package net.pingfang.network.dll.lp.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 加载动态库 通过将资源路径下的动态库拷贝到临时文件夹达到一致打包的效果
 * </p>
 *
 * @author 王超
 * @since 2022-09-16 14:38
 */
@Slf4j
public class LoadLibUtils {

	public static String loadLib(String libPath, String tmpdir) {
		try {
			Map<String, InputStream> inputStreamMap = getDirResource(libPath);
			inputStreamMap.forEach((key, value) -> {
				if (!StringUtils.isEmpty(key) && value != null) {
					copy(tmpdir, key, value);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpdir + "/" + libPath;
	}

	private synchronized static void copy(String tmpdir, String libName, InputStream in) {

		BufferedInputStream reader;
		FileOutputStream writer = null;
		libName = libName.replace("/", "\\");
		String filePath = tmpdir + libName;
		File extractedLibFile = new File(filePath);
		if (!extractedLibFile.getParentFile().exists()) {
			extractedLibFile.getParentFile().mkdirs();
		}

		if (extractedLibFile.exists()) {
			extractedLibFile.delete();
		}
		if (filePath.endsWith(File.separator)) {
			extractedLibFile.mkdirs();
			return;
		}

		try {
			reader = new BufferedInputStream(in);
			writer = new FileOutputStream(extractedLibFile);

			byte[] buffer = new byte[1024];

			while (reader.read(buffer) > 0) {
				writer.write(buffer);
				buffer = new byte[1024];
			}
		} catch (IOException e) {
			log.error("拷贝文件失败", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("I/O异常:", e);
				}
			}
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					log.error("I/O异常:", e);
				}
			}
		}
	}

	public static Map<String, InputStream> getDirResource(String dirPath) throws IOException {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		dirPath = dirPath.replace("\\", "/");
		Resource[] resources = resolver.getResources("classpath*:" + dirPath + "**");
		LinkedHashMap<String, InputStream> map = Maps.newLinkedHashMap();
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			try {
				InputStream inputStream = resources[i].getInputStream();
				String url = resource.getURL().toString();
				url = url.substring(url.indexOf(dirPath));
				map.put(url, inputStream);
			} catch (Exception e) {
				log.error("无法获取输入流：{}；{}", resource.getFilename(), e);
			}
		}
		return map;
	}

}
