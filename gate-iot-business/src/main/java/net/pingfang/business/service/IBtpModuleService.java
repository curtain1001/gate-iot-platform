package net.pingfang.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpModule;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-05 10:20
 */
public interface IBtpModuleService extends IService<BtpModule> {
	boolean save(BtpModule btpModule);

	boolean updateById(BtpModule btpModule);

	boolean removeById(Long id);

	boolean open(Long id);

	boolean close(Long id);

	List<BtpModule> selectByLaneId(long laneId);

	BtpModule selectByCode(long laneId, String moduleCode);
}
