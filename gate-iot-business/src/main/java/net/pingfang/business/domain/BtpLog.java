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
 * 设备与服务对接日志
 * </p>
 *
 * @author 王超
 * @since 2022-09-21 9:10
 */
@SuperBuilder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_log")
public class BtpLog extends BaseEntity {
	private static final long serialVersionUID = 279808762905648532L;
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 对象id
	 */
	private String objectId;

	private String type;

	private String data;

}
