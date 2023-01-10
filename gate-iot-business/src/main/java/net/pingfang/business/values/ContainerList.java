package net.pingfang.business.values;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 1:02
 */
@Data
@Builder
public class ContainerList {
	private String containerDoorDirection;
	private String containerIso;
	private int containerMaximumWeight;
	private String containerNo;
	private String containerPosition;
	private Date containerReliability;
	private boolean containerVerification;
}
