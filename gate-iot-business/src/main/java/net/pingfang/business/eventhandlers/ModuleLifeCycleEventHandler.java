package net.pingfang.business.eventhandlers;

import javax.annotation.Resource;

import com.google.common.eventbus.Subscribe;

import net.pingfang.business.domain.BtpModule;
import net.pingfang.business.events.ModuleClosedEvent;
import net.pingfang.business.events.ModuleCreatedEvent;
import net.pingfang.business.events.ModuleDeletedEvent;
import net.pingfang.business.events.ModuleStartedEvent;
import net.pingfang.business.events.ModuleUpdatedEvent;
import net.pingfang.business.manager.ClientModuleManager;
import net.pingfang.business.service.IBtpModuleService;
import net.pingfang.common.event.EventBusListener;
import net.pingfang.gate.protocol.values.LifeCycle;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-23 10:27
 */
@EventBusListener
public class ModuleLifeCycleEventHandler {
	@Resource
	private ClientModuleManager clientModuleManager;
	@Resource
	private IBtpModuleService btpModuleService;

	@Subscribe
	public void on(ModuleCreatedEvent event) {
		BtpModule btpModule = btpModuleService.selectByCode(event.getLaneId(), event.getModuleCode());
		clientModuleManager.toClient(btpModule, LifeCycle.create);
	}

	@Subscribe
	public void on(ModuleUpdatedEvent event) {
		BtpModule btpModule = btpModuleService.selectByCode(event.getLaneId(), event.getModuleCode());
		clientModuleManager.toClient(btpModule, LifeCycle.update);
	}

	@Subscribe
	public void on(ModuleDeletedEvent event) {
		BtpModule btpModule = btpModuleService.selectByCode(event.getLaneId(), event.getModuleCode());
		clientModuleManager.toClient(btpModule, LifeCycle.remove);
	}

	@Subscribe
	public void on(ModuleStartedEvent event) {
		BtpModule btpModule = btpModuleService.selectByCode(event.getLaneId(), event.getModuleCode());
		clientModuleManager.toClient(btpModule, LifeCycle.open);
	}

	@Subscribe
	public void on(ModuleClosedEvent event) {
		BtpModule btpModule = btpModuleService.selectByCode(event.getLaneId(), event.getModuleCode());
		clientModuleManager.toClient(btpModule, LifeCycle.close);
	}
}
