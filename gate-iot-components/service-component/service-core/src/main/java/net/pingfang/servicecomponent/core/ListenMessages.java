package net.pingfang.servicecomponent.core;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.pingfang.network.NetworkManager;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-30 10:24
 */
@Component
public class ListenMessages {
	@Resource
	NetworkManager networkManager;

}
