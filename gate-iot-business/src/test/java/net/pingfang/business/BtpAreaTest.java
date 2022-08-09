package net.pingfang.business;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import org.apache.commons.compress.utils.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.test.autoconfigure.MybatisPlusTest;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.pingfang.business.domain.BtpArea;
import net.pingfang.business.mapper.BtpAreaMapper;
import net.pingfang.business.service.IBtpAreaService;
import net.pingfang.business.service.impl.BtpAreaServiceImpl;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 16:06
 */
@RunWith(SpringRunner.class)
@MapperScan("net.pingfang.**.mapper")
@MybatisPlusTest(includeFilters = @ComponentScan.Filter(value = { BtpAreaServiceImpl.class, BtpAreaMapper.class }))
@ContextConfiguration(classes = { BtpAreaServiceImpl.class, BtpAreaMapper.class })
public class BtpAreaTest {
	@Autowired
	private BtpAreaMapper btpAreaMapper;
	@Autowired
	private IBtpAreaService btpAreaService;

//	@Test
	public void insert() {
		BtpArea area = BtpArea.builder() //
				.areaName("111") //
				.areaNo("111") //
				.createBy("111")//
				.createTime(new Date())//
				.remark("1111")//
				.build();
		int i = btpAreaMapper.insert(area);
		System.out.println(i);
	}

	@Before
	public void batchInsert() {
		List<BtpArea> areas = Lists.newArrayList();
		for (int i = 0; i < 20; i++) {
			BtpArea area = BtpArea.builder() //
					.areaName("测试" + i) //
					.areaNo(String.valueOf(i)) //
					.createBy("创建人" + i)//
					.createTime(new Date())//
					.remark("备注" + i)//
					.build();
			areas.add(area);
		}
		boolean count = btpAreaService.saveBatch(areas);
		assertTrue(count);
	}

	@Test
	public void pageTest() {
		BtpArea area = BtpArea.builder() //
				.areaName("测试") //
				.build();

		PageHelper.startPage(1, 10);
		LambdaQueryWrapper<BtpArea> queryWrapper = Wrappers.lambdaQuery(area);
		List<BtpArea> list = btpAreaService.list(queryWrapper);
		PageInfo<BtpArea> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		System.out.println("total:" + total);
		int pages = pageInfo.getPages();
		System.out.println("pages:" + pages);
		int pageSize = pageInfo.getPageSize();
		System.out.println("pageSize:" + pageSize);
		int count = pageInfo.getList().size();
		System.out.println("count:" + count);
	}

}
