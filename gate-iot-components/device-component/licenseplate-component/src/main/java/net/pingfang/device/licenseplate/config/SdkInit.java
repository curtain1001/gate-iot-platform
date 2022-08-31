package net.pingfang.device.licenseplate.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.licenseplate.values.ResultCode;
import net.sdk.function.main.NET;
import net.sdk.function.main.NetPath;

/**
 * @author 王超
 * @description TODO
 * @date 2022-04-22 12:01
 */
@Component
@Slf4j
public class SdkInit {

	public static NET net = null;

	@PostConstruct
	public void init() {
		String osName = System.getProperties().getProperty("os.name");
		if (osName.equals("Linux")) {
			log.error("软件运行环境错误,适用windows;当前环境：" + osName);
			Runtime.getRuntime().halt(0);
		}
		// 获取程序运行路径
		String path = "\\sdk";
		if (System.getProperty("java.vm.name") == null || !System.getProperty("java.vm.name").contains("64-Bit")) {
			path = path + "\\winSdk_x86\\";
		} else {
			path = path + "\\winSdk_64\\";
		}
		NetPath.DLL_PATH = path;
		net = NET.INSTANCE;
		int init = net.Net_Init();
		if (init != 0) {
			log.error("相机驱动初始化失败程序退出：" + ResultCode.getMsg(init));
			Runtime.getRuntime().halt(0);
		}
	}

	@PreDestroy
	public static void shutdownHook() {
		net.Net_UNinit();
		log.warn("程序退出,释放sdk资源");
	}

}
