package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpFlow;
import net.pingfang.business.mapper.BtpFlowMapper;
import net.pingfang.business.service.IBtpFlowService;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 17:20
 */
@Service
public class BtpFlowService extends ServiceImpl<BtpFlowMapper, BtpFlow> implements IBtpFlowService {
}
