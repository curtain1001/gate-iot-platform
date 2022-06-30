package net.pingfang.device.plc;

import javax.annotation.Resource;

import org.junit.Test;

import net.pingfang.network.NetworkManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:24
 */
//@RunWith(SpringRunner.class)
//@Slf4j
//@SpringBootTest(classes = TestApplication.class)
public class PLCDeviceTest {

	@Resource
	public NetworkManager networkManager;

	@Test
	public void constructorTest() {
//		PLCDevice device = new PLCDevice("TCP_CLIENT::001", "111", "eee", "eee", null, networkManager);
//		log.info(device.getDeviceId());
	}

}
