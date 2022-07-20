package net.pingfang.business.components;

import org.junit.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import net.pingfang.common.utils.JsonUtils;
import net.pingfang.iot.common.customizedsetting.repos.CustomizedSettingRepository;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-23 10:05
 */
@RunWith(SpringRunner.class)
public class CustomizedSettingTest {

	private final Logger logger = LoggerFactory.getLogger(CustomizedSettingTest.class);

	@Test
	public void list() {
		logger.info(() -> JsonUtils.toJsonString(CustomizedSettingRepository.getValues()));
	}
}
