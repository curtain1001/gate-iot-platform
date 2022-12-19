package net.pingfang.business.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpLane;
import net.pingfang.business.mapper.BtpLaneMapper;
import net.pingfang.business.service.IBtpLaneService;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-22 15:46
 */
@Service
public class BtpLaneServiceImpl extends ServiceImpl<BtpLaneMapper, BtpLane> implements IBtpLaneService {
	@Resource
	BtpLaneMapper btpLaneMapper;

	@Override
	public BtpLane load(String laneNo) {
		LambdaQueryWrapper<BtpLane> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpLane::getLaneNo, laneNo);
		return btpLaneMapper.selectOne(queryWrapper);
	}
}
