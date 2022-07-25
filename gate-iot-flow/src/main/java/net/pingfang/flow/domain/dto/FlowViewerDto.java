package net.pingfang.flow.domain.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * @author Xuan xuan
 * @date 2021/4/21 20:55
 */
@Data
public class FlowViewerDto implements Serializable {

	private String key;
	private boolean completed;
}
