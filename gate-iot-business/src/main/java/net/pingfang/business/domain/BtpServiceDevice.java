package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 服务关联设备
 * </p>
 *
 * @author 王超
 * @since 2022-10-25 17:41
 */
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_support_service_device", autoResultMap = true)
public class BtpServiceDevice {
	@TableId(type = IdType.AUTO)
	private Long id;

	private Long serviceId;

	private Long deviceId;
}
