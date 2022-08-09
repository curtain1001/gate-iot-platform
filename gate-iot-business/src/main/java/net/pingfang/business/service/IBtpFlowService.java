package net.pingfang.business.service;

import com.baomidou.mybatisplus.extension.service.IService;

import net.pingfang.business.domain.BtpFlow;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 17:20
 */
public interface IBtpFlowService extends IService<BtpFlow> {
	boolean deploy(Long laneId, Long flowId, int version);

}
