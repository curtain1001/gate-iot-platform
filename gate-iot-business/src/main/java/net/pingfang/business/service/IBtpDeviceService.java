package net.pingfang.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpDevice;

/**
 * @author 王超
 * @description TODO
 * @date 2022-07-04 17:00
 */
public interface IBtpDeviceService extends IService<BtpDevice> {

	boolean save(BtpDevice device);

	boolean updateById(BtpDevice device);

	boolean removeById(Long id);

	boolean open(Long id);

	boolean close(Long id);

	List<BtpDevice> selectByLaneNo(String laneNo);

	List<BtpDevice> selectByLaneId(Long laneId);

}
