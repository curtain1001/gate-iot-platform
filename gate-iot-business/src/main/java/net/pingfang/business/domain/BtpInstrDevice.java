package net.pingfang.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * @author 王超
 * @description 设备命令关联表
 * @date 2022-06-29 0:10
 */
@TableName("btp_instr_device")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BtpInstrDevice extends BaseEntity {
	private static final long serialVersionUID = -7925968188970929558L;
	@TableId(type = IdType.AUTO)
	private Long instrDeviceId;
	/**
	 * 设备id
	 */
	private Long deviceId;
	/**
	 * 指令id
	 */
	private Long instrId;

}
