package net.pingfang.business.component.customizedsetting.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedSettingData {
	final String id;
	final String customizeType;
	final AttributeEnum attribute;
	final Dosage dosage;
}
