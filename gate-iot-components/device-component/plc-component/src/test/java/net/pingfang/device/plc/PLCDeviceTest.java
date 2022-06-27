package net.pingfang.device.plc;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.network.NetworkManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 17:24
 */
@RunWith(SpringRunner.class)
@Slf4j
//@MapperScan("net.pingfang.**.mapper")

@SpringBootTest(classes = TestApplication.class)
class PLCDeviceTest {

	@Resource
	public NetworkManager networkManager;

	@Test
	public void constructorTest() {
		PLCDevice device = new PLCDevice("111", "111", "eee", "eee", null, networkManager);
		log.info(device.getDeviceId());
	}

}
