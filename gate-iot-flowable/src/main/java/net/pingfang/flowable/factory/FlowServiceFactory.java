package net.pingfang.flowable.factory;

import javax.annotation.Resource;

import org.flowable.engine.FormService;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.ManagementService;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * flowable 引擎注入封装
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Component
@Getter
public class FlowServiceFactory {

	@Resource
	protected RepositoryService repositoryService;

	@Resource
	protected RuntimeService runtimeService;

	@Resource
	protected IdentityService identityService;

	@Resource
	protected TaskService taskService;

	@Resource
	protected FormService formService;

	@Resource
	protected HistoryService historyService;

	@Resource
	protected ManagementService managementService;

	@Qualifier("processEngine")
	@Resource
	protected ProcessEngine processEngine;

}
