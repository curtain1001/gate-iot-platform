package net.pingfang.business.component.customizedsetting.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;
import net.pingfang.business.component.customizedsetting.Customized;

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
}
