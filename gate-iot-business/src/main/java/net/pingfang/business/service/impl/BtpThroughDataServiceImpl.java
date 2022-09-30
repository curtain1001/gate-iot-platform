package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

import net.pingfang.business.domain.BtpThroughData;
import net.pingfang.business.mapper.BtpThroughDataMapper;
import net.pingfang.business.service.IBtpThroughDataService;
import net.pingfang.iot.common.manager.ThroughDataManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-09-21 16:24
 */
@Service
public class BtpThroughDataServiceImpl extends ServiceImpl<BtpThroughDataMapper, BtpThroughData>
		implements IBtpThroughDataService, ThroughDataManager {
	@Override
	public JsonNode getData(Long laneNo) {
		return null;
	}

	@Override
	public void putData(JsonNode jsonNode) {

	}
}
