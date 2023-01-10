package net.pingfang.business.values;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 10:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EquipmentStateMsg {
	private String cpuDescribe;
	private Date cpuUse;
	private List<DevicesList> devicesList;
	private String diskDescribe;
	private String diskUse;
	private String laneNo;
	private String memoryDescribe;
	private Date memoryUse;
	private String messageType;
	private String serverIp;
	private String serverName;
	private Date updateTime;
}
