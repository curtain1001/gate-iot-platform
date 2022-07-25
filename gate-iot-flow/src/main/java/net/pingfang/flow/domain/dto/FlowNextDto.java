package net.pingfang.flow.domain.dto;

import java.io.Serializable;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;

import lombok.Data;

/**
 * 动态人员、组
 *
 * @author Xuan xuan
 * @date 2021/4/17 22:59
 */
@Data
public class FlowNextDto implements Serializable {

	private String type;

	private String vars;

	private List<SysUser> userList;

	private List<SysRole> roleList;
}
