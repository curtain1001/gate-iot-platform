package net.pingfang.business.manager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpLaneConfig;
import net.pingfang.business.service.IBtpLaneConfigService;
import net.pingfang.device.core.manager.LaneConfigManager;
import net.pingfang.iot.common.customizedsetting.Customized;
import net.pingfang.iot.common.customizedsetting.values.DefaultCustomized;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-13 18:09
 */
@Component
public class DefaultLaneConfigManager implements LaneConfigManager {
	@Resource
	public IBtpLaneConfigService laneConfigService;

	@Override
	public Map<Customized, Object> getConfig(Long laneId) {
		LambdaQueryWrapper<BtpLaneConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpLaneConfig::getLaneId, laneId);
		List<BtpLaneConfig> btpLaneConfigList = laneConfigService.list(queryWrapper);
		return btpLaneConfigList.stream().filter(x -> "0".equals(x.getStatus())).collect(Collectors
				.toMap(x -> DefaultCustomized.valueOf(x.getLaneConfigKey()), BtpLaneConfig::getLaneConfigValue));
	}

	@Override
	public String getConfig(Customized value, Long laneId) {
		LambdaQueryWrapper<BtpLaneConfig> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpLaneConfig::getLaneId, laneId);
		queryWrapper.eq(BtpLaneConfig::getLaneConfigKey, value.getValue());
		BtpLaneConfig config = laneConfigService.getOne(queryWrapper);
		return config.getLaneConfigValue();
	}
}
