package net.pingfang.device.plc;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.iot.common.customizedsetting.values.CustomizedSettingData;
import net.pingfang.iot.common.product.Product;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.network.DefaultNetworkType;
import net.pingfang.network.tcp.parser.PayloadParserType;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-12 15:33
 */
public enum PLCProduct implements Product {
	PLC("PLC");

	private final String name;

	PLCProduct(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return this.name();
	}

	@Override
	public DefaultNetworkType getNetwork() {
		return DefaultNetworkType.TCP_CLIENT;
	}

	@Override
	public Map<String, Object> getDefaultProperties() {
		Map<String, Object> properties = Maps.newHashMap();
		properties.put("parserType", PayloadParserType.FIXED_LENGTH);
		properties.put("parserConfiguration", Collections.singletonMap("size", 4));
		return properties;
	}

	@Override
	public List<CustomizedSettingData> getBasicForm() {
		return CustomizedSettingRepository.getValues(PLCBasicFormCustomized.values());
	}

	@Component
	private static class PLCSupportRegistry {
		@PostConstruct
		public void init() {
			ProductSupports.registry(PLC);
		}
	}

}
