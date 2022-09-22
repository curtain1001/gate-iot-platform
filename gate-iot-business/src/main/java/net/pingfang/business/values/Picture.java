package net.pingfang.business.values;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 图片信息
 * </p>
 *
 * @author 王超
 * @since 2022-09-20 10:09
 */
@Data
@Builder(toBuilder = true)
public class Picture {
	final String rightBackDamagePictureUrl;
	final String framePictureUrl;
	final String vehiclePictureUrl;
	final String backTopPictureUrl;
	final String vehicleBottomPictureUrl;
	final String rightMiddlePictureUrl;
	final String rightFrontPictureUrl;
	final String leftMiddlePictureUrl;
	final String framePlatePictureUrl;
	final String leftFrontPictureUrl;
	final String rightBackPictureUrl;
	final String topMosaicPictureUrl;
	final String rightFrontDamagePictureUrl;
	final String leftBackDamagePictureUrl;
	final String licencePlatePictureUrl;
	final String leftMosaicPictureUrl;
	final String frontTopPictureUrl;
	final String leftFrontDamagePictureUrl;
	final String leftBackPictureUrl;
	final String rightMosaicPictureUrl;
	/**
	 * 残损标注
	 */
	final List<ContainerDamageMark> containerDamageMarkList;
}
