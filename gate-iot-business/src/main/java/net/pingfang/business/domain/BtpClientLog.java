package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-11-23 14:37
 */
@TableName("btp_certificate")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder(toBuilder = true)
public class BtpClientLog extends BaseEntity {

	private static final long serialVersionUID = 467005048840118839L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 车道id
	 */
	private Long laneId;

	/**
	 * 设备号
	 */
	private String deviceNo;
//    /**
//     *
//     */
//    private String

}
