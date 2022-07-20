package net.pingfang.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpArea;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 15:56
 */

public interface IBtpAreaService extends IService<BtpArea> {
	/**
	 * 获取所有场站及场站的通道信息
	 *
	 * @return
	 */
	List<BtpArea> getAll();
}
