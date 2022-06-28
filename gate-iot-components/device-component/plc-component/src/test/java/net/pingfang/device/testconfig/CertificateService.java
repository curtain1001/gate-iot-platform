package net.pingfang.device.testconfig;

import org.springframework.stereotype.Component;

import net.pingfang.network.security.Certificate;
import net.pingfang.network.security.CertificateManager;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-27 18:26
 */
@Component
public class CertificateService implements CertificateManager {

	@Override
	public Certificate getCertificate(String id) {
		return null;
	}
}
