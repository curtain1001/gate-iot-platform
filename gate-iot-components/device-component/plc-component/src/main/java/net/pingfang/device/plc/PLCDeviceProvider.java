package net.pingfang.device.plc;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.device.core.DeviceOperator;
import net.pingfang.device.core.DeviceProperties;
import net.pingfang.device.core.DeviceProvider;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.product.DeviceProduct;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.client.TcpClient;
import net.pingfang.network.tcp.client.TcpClientNetworkType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-11 17:38
 */
@Slf4j
@Component
public class PLCDeviceProvider implements DeviceProvider<PLCDeviceProperties> {
	@Resource
	private NetworkManager networkManager;
	@Resource
	private InstructionManager instructionManager;

	@Override
	public DeviceProduct getType() {
		return PLCDeviceProduct.PLC;
	}

	@Override
	public DeviceOperator createDevice(PLCDeviceProperties properties) {
		PLCDevice device = new PLCDevice(properties.getLaneId(), properties.getId(), properties.getName(),
				networkManager, instructionManager);
		return init(device, properties);
	}

	public PLCDevice init(PLCDevice plcDevice, PLCDeviceProperties properties) {
		TcpClient tcpClient = (TcpClient) networkManager.getNetwork(TcpClientNetworkType.TCP_CLIENT,
				properties.getId());
		plcDevice.setTcpClient(tcpClient);
		return plcDevice;
	}

	@Override
	public void reload(DeviceOperator operator, PLCDeviceProperties properties) {
		init((PLCDevice) operator, properties);
	}

	@Override
	public PLCDeviceProperties createConfig(DeviceProperties properties) {
		PLCDeviceProperties config = new PLCDeviceProperties();
		try {
			BeanUtils.populate(config, properties.getConfiguration());
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("PLC设备配置拷贝失败：", e);
		}
		config.setId(properties.getDeviceId());
		config.setLaneId(properties.getLaneId());
		config.setName(properties.getDeviceName());
		return config;
	}

}
