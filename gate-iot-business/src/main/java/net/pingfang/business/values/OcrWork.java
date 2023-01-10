package net.pingfang.business.values;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 1:01
 */
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class OcrWork {
	private int areaNo;
	private Date collectionTime;
	private boolean containerIsDamage;
	private List<ContainerList> containerList;
	private int containerTotal;
	private String customsNo;
	private Driver driver;
	private String framePlate;
	private String inExitFlag;
	private String laneNo;
	private String licensePlate;
	private String licensePlateColour;
	private String messageType;
	private Picture picture;
	private String rfidPlate;
	private String uniqueNumber;
	private int weight;
	private String wheelHubMark;
	private int wheelHubTotal;
}
