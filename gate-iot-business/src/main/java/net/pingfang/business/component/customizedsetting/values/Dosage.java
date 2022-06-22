package net.pingfang.business.component.customizedsetting.values;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Dosage {
	String type;
	Object value;
	String label;
	Object options;
}
