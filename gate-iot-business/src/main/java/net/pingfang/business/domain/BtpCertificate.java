package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-28 15:07
 */
@TableName("btp_certificate")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
public class BtpCertificate extends BaseEntity {

	private static final long serialVersionUID = 5299512958289871444L;

	@TableId(type = IdType.AUTO)
	private Long certId;

	private String name;

//	private CertificateType instance;
	private String instance;

	private CertificateConfig configs;

	@Getter
	@Setter
	public static class CertificateConfig {
		/**
		 * 证书内容(base64)
		 */
		private String keystoreBase64;

		/**
		 * 信任库内容(base64)
		 */
		private String trustKeyStoreBase64;

		/**
		 * 证书密码
		 */
		private String keystorePwd;

		/**
		 * 信任库密码
		 */
		private String trustKeyStorePwd;
	}
}
