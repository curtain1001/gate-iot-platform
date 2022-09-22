package net.pingfang.business.values;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 司机信息
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:26
 */
@Data
@Builder(toBuilder = true)
public class DriverInfo {
	/**
	 * 姓名
	 */
	String driverName;
	/**
	 * 身份证号
	 */
	String driverIdentityCard;
	/**
	 * 手机号
	 */
	String driverPhone;
	/**
	 * 家庭住址
	 */
	String driverAddress;
}
