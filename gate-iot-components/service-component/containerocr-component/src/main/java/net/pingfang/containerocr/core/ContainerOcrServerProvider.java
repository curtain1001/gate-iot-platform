package net.pingfang.containerocr.core;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.containerocr.ContainerOcrProduct;
import net.pingfang.iot.common.instruction.InstructionManager;
import net.pingfang.iot.common.product.Product;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.NetworkManager;
import net.pingfang.servicecomponent.core.ServerOperator;
import net.pingfang.servicecomponent.core.ServerProperties;
import net.pingfang.servicecomponent.core.ServerProvider;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-15 15:45
 */
@Component
@Slf4j
public class ContainerOcrServerProvider implements ServerProvider<ContainerOcrProperties> {
	@Resource
	private NetworkManager networkManager;
	@Resource
	private InstructionManager instructionManager;

	@Override
	public Product getType() {
		return ContainerOcrProduct.CONTAINER_OCR;
	}

	@Override
	public ServerOperator createServer(ContainerOcrProperties properties) {
		ContainerOcrServer ocrServer = new ContainerOcrServer(properties.getLaneId(), properties.getId(),
				properties.getName(), networkManager, instructionManager);
		return init(ocrServer, properties);
	}

	public ContainerOcrServer init(ContainerOcrServer ocrServer, ContainerOcrProperties properties) {
		try {
			networkManager.getNetwork(DefaultNetworkType.TCP_SERVER, properties.getId());
		} catch (Exception e) {
			log.error("服务支持网络组件启动失败：", e);
		}
		return ocrServer;
	}

	@Override
	public void reload(ServerOperator operator, ContainerOcrProperties properties) {
		init((ContainerOcrServer) operator, properties);
	}

	@Override
	public ContainerOcrProperties createConfig(ServerProperties properties) {
		ContainerOcrProperties config = new ContainerOcrProperties();
		try {
			BeanUtils.populate(config, properties.getConfiguration());
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("车牌识别设备配置拷贝失败：", e);
		}
		config.setId(properties.getServerId());
		config.setLaneId(properties.getLaneId());
		config.setName(properties.getServerName());
		return config;
	}
}
