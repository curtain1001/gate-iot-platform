package net.pingfang.network.dll.lp.config;

import java.io.File;

import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.RuntimeUtils;
import net.pingfang.network.dll.lp.utils.LoadLibUtils;
import net.pingfang.network.dll.lp.values.ResultCode;
import net.sdk.function.main.NET;
import net.sdk.function.main.NetPath;

/**
 * @author 王超
 * @description TODO
 * @date 2022-04-22 12:01
 */
@Slf4j
public class SdkInit {

	public static NET net = null;

	public static void init() {
		String osName = System.getProperties().getProperty("os.name");
		if (osName.equals("Linux")) {
			log.error("软件运行环境错误,适用windows;当前环境：" + osName);
			Runtime.getRuntime().halt(0);
		}
		// 获取程序运行路径
		String path = "sdk";
		if (System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").contains("64-Bit")) {
			path = path + File.separator + "winSdk_x86" + File.separator;
		} else {
			path = path + File.separator + "winSdk_64" + File.separator;
		}
		if (RuntimeUtils.isStartupFromJar()) {
			String tempPath = System.getProperty("java.io.tmpdir");
			try {
				LoadLibUtils.loadLib(path, tempPath);
				NetPath.DLL_PATH = tempPath + path;
			} catch (Exception e) {
				log.error("加载动态库失败；", e);
			}
		} else {
			NetPath.DLL_PATH = path;
		}
		try {
			net = NET.INSTANCE;
			int init = net.Net_Init();
			if (init != 0) {
				log.error("相机驱动初始化失败程序退出：" + ResultCode.getMsg(init));
				Runtime.getRuntime().halt(0);
			}
		} catch (Exception e) {
			log.error("相机驱动初始化失败：", e);
		}

	}

	@PreDestroy
	public static void shutdownHook() {
		net.Net_UNinit();
		log.warn("程序退出,释放sdk资源");
	}

}
