package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpLaneConfig;
import net.pingfang.business.mapper.BtpLaneConfigMapper;
import net.pingfang.business.service.IBtpLaneConfigService;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-23 14:44
 */
@Service
public class BtpLaneConfigServiceImpl extends ServiceImpl<BtpLaneConfigMapper, BtpLaneConfig>
		implements IBtpLaneConfigService {
}
