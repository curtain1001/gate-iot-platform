package net.pingfang.iot.common.customizedsetting.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import net.pingfang.iot.common.customizedsetting.Customized;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomizedSettingData {
	final String customizeType;
	final Customized attribute;
	final String type;
	final Object value;
	final String label;
	final Object options;
	final Object defaults;
}
