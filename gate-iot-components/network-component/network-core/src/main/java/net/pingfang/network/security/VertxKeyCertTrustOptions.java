package net.pingfang.network.security;

import java.util.function.Function;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import io.vertx.core.Vertx;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.TrustOptions;
import lombok.SneakyThrows;

public class VertxKeyCertTrustOptions implements KeyCertOptions, TrustOptions {

	private Certificate certificate;

	@SneakyThrows
	public VertxKeyCertTrustOptions(Certificate certificate) {
		this.certificate = certificate;
	}

	@Override
	@SneakyThrows
	public VertxKeyCertTrustOptions clone() {
		return new VertxKeyCertTrustOptions(certificate);
	}

	@Override
	public VertxKeyCertTrustOptions copy() {
		return clone();
	}

	@Override
	public KeyManagerFactory getKeyManagerFactory(Vertx vertx) {
		return certificate.getKeyManagerFactory();
	}

	@Override
	public Function<String, X509KeyManager> keyManagerMapper(Vertx vertx) {
		return certificate::getX509KeyManager;
	}

	@Override
	public TrustManagerFactory getTrustManagerFactory(Vertx vertx) {
		return certificate.getTrustManagerFactory();
	}

	@Override
	public Function<String, TrustManager[]> trustManagerMapper(Vertx vertx) {
		return certificate::getTrustManager;
	}
}
