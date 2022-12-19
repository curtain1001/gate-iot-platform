package net.pingfang.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.customizedsetting.repos.CustomizedSettingRepository;
import net.pingfang.common.customizedsetting.values.DefaultCustomized;
import net.pingfang.common.utils.JsonUtils;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-22 17:44
 */
@RunWith(SpringRunner.class)
@MapperScan("net.pingfang.**.mapper")
@MybatisPlusTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION))
@Slf4j
public class BtpLaneTest {

	@Test
	public void laneConfigTest() {
		log.info(JsonUtils.toJsonString(CustomizedSettingRepository.getValues(DefaultCustomized.values())));
	}
}
