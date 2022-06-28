package net.pingfang.business.manager;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.pingfang.business.domain.BtpCertificate;
import net.pingfang.business.service.IBtpCertificateService;
import net.pingfang.network.security.Certificate;
import net.pingfang.network.security.CertificateManager;
import net.pingfang.network.security.DefaultCertificate;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 11:01
 */
@Component
public class DefaultCertificateManager implements CertificateManager {
	@Resource
	IBtpCertificateService btpCertificateService;

	@Override
	public Certificate getCertificate(String id) {
		BtpCertificate btpCertificate = btpCertificateService.getById(id);
		DefaultCertificate defaultCertificate = new DefaultCertificate(btpCertificate.getCertId(),
				btpCertificate.getName());
		return btpCertificate.getInstance().init(defaultCertificate, btpCertificate.getConfigs());
	}
}
