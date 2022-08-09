package net.pingfang.business.domain;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.pingfang.common.core.domain.BaseEntity;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-01 17:17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "btp_flow", autoResultMap = true)
public class BtpFlow extends BaseEntity {
	private static final long serialVersionUID = 2996218050404312827L;
	/**
	 * 流程id
	 */
	@TableId(type = IdType.AUTO)
	private Long flowId;
	/**
	 * 车道id
	 */
	private Long laneId;
	/**
	 * 流程名称
	 */
	private String flowName;
	/**
	 * 流程前端数据
	 */
	@TableField(jdbcType = JdbcType.VARCHAR, javaType = true, typeHandler = JacksonTypeHandler.class)
	private JsonNode content;

	@Version
	private Integer version;

	private transient DeployInfo deployInfo;

	/**
	 * 部署信息
	 */
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	static class DeployInfo {
		Long deployId;
		int version;
	}
}
