package net.pingfang.network.dll.lp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicensePlateClientProperties {

	private String id;

	private String host;

	private int port;

}
