package net.pingfang.business.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.business.domain.BtpCertificate;
import net.pingfang.business.mapper.BtpCertificateMapper;
import net.pingfang.business.service.IBtpCertificateService;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 15:21
 */
@Service
public class BtpCertificateServiceImpl extends ServiceImpl<BtpCertificateMapper, BtpCertificate>
		implements IBtpCertificateService {
}
