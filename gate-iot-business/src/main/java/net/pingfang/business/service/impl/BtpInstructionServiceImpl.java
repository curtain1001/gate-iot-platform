package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpInstruction;
import net.pingfang.business.mapper.BtpInstructionMapper;
import net.pingfang.business.service.IBtpInstructionService;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 23:42
 */
@Service
public class BtpInstructionServiceImpl extends ServiceImpl<BtpInstructionMapper, BtpInstruction>
		implements IBtpInstructionService {
}
