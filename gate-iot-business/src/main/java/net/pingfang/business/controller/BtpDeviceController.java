package net.pingfang.business.controller;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.vertx.core.net.NetClientOptions;
import net.pingfang.business.domain.BtpDevice;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.device.core.Product;
import net.pingfang.device.plc.PLCDevice;
import net.pingfang.network.NetworkManager;
import net.pingfang.network.tcp.TcpMessage;
import net.pingfang.network.tcp.client.TcpClientProperties;
import net.pingfang.network.tcp.client.VertxTcpClient;
import net.pingfang.network.tcp.client.VertxTcpClientProvider;
import net.pingfang.network.tcp.parser.PayloadParserType;

/**
 * @author 王超
 * @description 设备管理
 * @date 2022-06-29 23:28
 */
@RestController
@RequestMapping("device")
public class BtpDeviceController {
	@Resource
	VertxTcpClientProvider provider;
	@Resource
	NetworkManager networkManager;

	@GetMapping("/product/list")
	public AjaxResult getDeviceProduct() {
		return AjaxResult.success(Product.getValues());
//		return AjaxResult.success();
	}

	@PostMapping
	public AjaxResult add(@RequestBody BtpDevice device) {
		TcpClientProperties properties = new TcpClientProperties();
		properties.setHost("127.0.0.1");
		properties.setPort(8082);
		properties.setParserType(PayloadParserType.DIRECT);
//		properties.setParserConfiguration(Collections.singletonMap("size", 4));
		properties.setOptions(new NetClientOptions());
		VertxTcpClient tcpClient = provider.createNetwork(properties);
		PLCDevice plcDevice = new PLCDevice(device.getDeviceId(), device.getLaneId(), "", device.getProduct().getName(),
				Maps.newHashMap(), tcpClient);
		TcpMessage tcpMessage = new TcpMessage();
		ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
		byteBuf.writeByte(0xFE);
		byteBuf.writeBytes("00".getBytes(StandardCharsets.UTF_8));
		byteBuf.writeByte(0xFF);
		tcpMessage.setPayload(byteBuf);
		plcDevice.send(tcpMessage);
		return AjaxResult.success();
	}

}
