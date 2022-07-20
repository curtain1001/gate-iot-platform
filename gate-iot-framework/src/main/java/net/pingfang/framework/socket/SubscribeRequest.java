package net.pingfang.framework.socket;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pingfang.common.core.domain.model.LoginUser;
import net.pingfang.common.utils.ValueObject;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscribeRequest implements ValueObject {

	private String id;

	private String topic;

	private Map<String, Object> parameter;

	@Override
	public Map<String, Object> values() {
		return parameter;
	}

	public static SubscribeRequest of(MessagingRequest request, LoginUser loginUser) {
		return SubscribeRequest.builder().id(request.getId()).topic(request.getTopic())
				.parameter(request.getParameter()).build();

	}
}
