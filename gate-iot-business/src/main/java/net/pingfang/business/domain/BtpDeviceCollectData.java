package net.pingfang.business.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-10-26 23:11
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
@TableName(value = "btp_device_collect_data", autoResultMap = true)
@Accessors(chain = true)
public class BtpDeviceCollectData extends BaseEntity {
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 设备id（采用String：设备id一般有特定命名规则）
	 */
	private String deviceId;

	/**
	 * 车道id
	 */
	private Long laneId;

	private Date collectTime;

	private String data;

}
