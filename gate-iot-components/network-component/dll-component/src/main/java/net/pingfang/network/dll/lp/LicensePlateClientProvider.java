package net.pingfang.network.dll.lp;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.sun.jna.Pointer;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.utils.bean.BeanUtils;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.network.NetworkType;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.Network;
import net.pingfang.network.NetworkProperties;
import net.pingfang.network.NetworkProvider;
import net.pingfang.network.dll.lp.config.SdkNet;
import net.pingfang.network.dll.lp.values.ResultCode;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-24 15:37
 */
@Component
@Slf4j
public class LicensePlateClientProvider implements NetworkProvider<LicensePlateClientProperties> {

	public LicensePlateClientProvider() {
		SdkNet.init();
	}

	@Override
	public NetworkType getType() {
		return DefaultNetworkType.LP_DLL;
	}

	@Override
	public LicensePlateClient createNetwork(LicensePlateClientProperties properties) {
		LicensePlateClient client = new LicensePlateClient(properties.getId(), properties.getLaneId());
		int handle = init(properties.getHost(), (short) properties.getPort(), (short) properties.getTimeout(), client);
		client.setHandle(handle);
		return client;
	}

	/**
	 * 初始化连接
	 *
	 * @param ip      IP地址
	 * @param port    端口号
	 * @param timeout 连接超时时间
	 */
	public int init(String ip, short port, short timeout, LicensePlateClient client) {
		int handle = -1;
		// 添加相机
		handle = SdkNet.net.Net_AddCamera(ip);
		if (handle != -1) {
			int conn = SdkNet.net.Net_ConnCamera(handle, port, timeout);
			if (conn != 0) {
				log.error("相机连接：" + ResultCode.getMsg(conn));
			}
			int rem = SdkNet.net.Net_RegReportMessEx(handle, client.reportCBEx(), Pointer.NULL);
			if (rem != 0) {
				throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rem));
			}
			int rev = SdkNet.net.Net_RegImageRecvEx(handle, client.imageCBEx(), Pointer.createConstant(port));
			if (rev != 0) {
				throw new RuntimeException("车牌识别结果获取回调函数注册完毕：" + ResultCode.getMsg(rev));
			}
		}
		return handle;
	}

	@Override
	public void reload(Network network, LicensePlateClientProperties properties) {
		LicensePlateClient plateClient = ((LicensePlateClient) network);
		int handle = init(properties.getHost(), (short) properties.getPort(), (short) properties.getTimeout(),
				plateClient);
		plateClient.setHandle(handle);
	}

	@Override
	public LicensePlateClientProperties createConfig(NetworkProperties properties)
			throws InvocationTargetException, IllegalAccessException {
		LicensePlateClientProperties config = new LicensePlateClientProperties();
		BeanUtils.copyBean(config, properties.getConfigurations());
		config.setId(properties.getId());
		config.setLaneId(properties.getLaneId());
		return config;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return Lists.newArrayList();
	}
}
