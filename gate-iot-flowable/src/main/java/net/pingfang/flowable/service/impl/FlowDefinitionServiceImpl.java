package net.pingfang.flowable.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.domain.entity.SysUser;
import net.pingfang.common.utils.SecurityUtils;
import net.pingfang.flowable.common.constant.ProcessConstants;
import net.pingfang.flowable.common.enums.FlowComment;
import net.pingfang.flowable.domain.dto.FlowProcDefDto;
import net.pingfang.flowable.factory.FlowServiceFactory;
import net.pingfang.flowable.service.IFlowDefinitionService;
import net.pingfang.flowable.service.ISysDeployFormService;
import net.pingfang.system.service.ISysDeptService;
import net.pingfang.system.service.ISysPostService;
import net.pingfang.system.service.ISysUserService;

/**
 * 流程定义
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Service
@Slf4j
public class FlowDefinitionServiceImpl extends FlowServiceFactory implements IFlowDefinitionService {

	@Resource
	private ISysDeployFormService sysDeployFormService;

	@Resource
	private ISysUserService sysUserService;

	@Resource
	private ISysDeptService sysDeptService;

	@Resource
	private ISysPostService postService;

	private static final String BPMN_FILE_SUFFIX = ".bpmn";

	@Override
	public boolean exist(String processDefinitionKey) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinitionKey);
		long count = processDefinitionQuery.count();
		return count > 0 ? true : false;
	}

	/**
	 * 流程定义列表
	 *
	 * @param pageNum  当前页码
	 * @param pageSize 每页条数
	 * @return 流程定义分页列表数据
	 */
	@Override
	public Page<FlowProcDefDto> list(Integer pageNum, Integer pageSize) {
		Page<FlowProcDefDto> page = new Page<>();
		// 流程定义列表数据查询
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
//                .latestVersion()
				.orderByProcessDefinitionKey().asc();
		page.setTotal(processDefinitionQuery.count());
		List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(pageNum - 1, pageSize);

		List<FlowProcDefDto> dataList = new ArrayList<>();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			FlowProcDefDto reProcDef = new FlowProcDefDto();
			BeanUtils.copyProperties(processDefinition, reProcDef);
			SysForm sysForm = sysDeployFormService.selectSysDeployFormByDeployId(deploymentId);
			if (Objects.nonNull(sysForm)) {
				reProcDef.setFormName(sysForm.getFormName());
				reProcDef.setFormId(sysForm.getFormId());
			}
			// 流程定义时间
			reProcDef.setDeploymentTime(deployment.getDeploymentTime());
			dataList.add(reProcDef);
		}
		page.setRecords(dataList);
		return page;
	}

	/**
	 * 导入流程文件
	 *
	 * @param name
	 * @param category
	 * @param in
	 */
	@Override
	public void importFile(String name, String category, InputStream in) {
		Deployment deploy = repositoryService.createDeployment().addInputStream(name + BPMN_FILE_SUFFIX, in).name(name)
				.category(category).deploy();
		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.singleResult();
		repositoryService.setProcessDefinitionCategory(definition.getId(), category);

	}

	/**
	 * 读取xml
	 *
	 * @param deployId
	 * @return
	 */
	@Override
	public AjaxResult readXml(String deployId) throws IOException {
		ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		InputStream inputStream = repositoryService.getResourceAsStream(definition.getDeploymentId(),
				definition.getResourceName());
		String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		return AjaxResult.success("", result);
	}

	/**
	 * 读取xml
	 *
	 * @param deployId
	 * @return
	 */
	@Override
	public InputStream readImage(String deployId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		// 获得图片流
		DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
		// 输出为图片
		return diagramGenerator.generateDiagram(bpmnModel, "png", Collections.emptyList(), Collections.emptyList(),
				"宋体", "宋体", "宋体", null, 1.0, false);

	}

	/**
	 * 根据流程定义ID启动流程实例
	 *
	 * @param procDefId 流程定义Id
	 * @param variables 流程变量
	 * @return
	 */
	@Override
	public AjaxResult startProcessInstanceById(String procDefId, Map<String, Object> variables) {
		try {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.processDefinitionId(procDefId).latestVersion().singleResult();
			if (Objects.nonNull(processDefinition) && processDefinition.isSuspended()) {
				return AjaxResult.error("流程已被挂起,请先激活流程");
			}
//           variables.put("skip", true);
//           variables.put(ProcessConstants.FLOWABLE_SKIP_EXPRESSION_ENABLED, true);
			// 设置流程发起人Id到流程中
			SysUser sysUser = SecurityUtils.getLoginUser().getUser();
			identityService.setAuthenticatedUserId(sysUser.getUserId().toString());
			variables.put(ProcessConstants.PROCESS_INITIATOR, "");
			ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDefId, variables);
			// 给第一步申请人节点设置任务执行人和意见 todo:第一个节点不设置为申请人节点有点问题？
			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId())
					.singleResult();
			if (Objects.nonNull(task)) {
				taskService.addComment(task.getId(), processInstance.getProcessInstanceId(),
						FlowComment.NORMAL.getType(), sysUser.getNickName() + "发起流程申请");
//                taskService.setAssignee(task.getId(), sysUser.getUserId().toString());
				taskService.complete(task.getId(), variables);
			}
			return AjaxResult.success("流程启动成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxResult.error("流程启动错误");
		}
	}

	/**
	 * 激活或挂起流程定义
	 *
	 * @param state    状态
	 * @param deployId 流程部署ID
	 */
	@Override
	public void updateState(Integer state, String deployId) {
		ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery().deploymentId(deployId)
				.singleResult();
		// 激活
		if (state == 1) {
			repositoryService.activateProcessDefinitionById(procDef.getId(), true, null);
		}
		// 挂起
		if (state == 2) {
			repositoryService.suspendProcessDefinitionById(procDef.getId(), true, null);
		}
	}

	/**
	 * 删除流程定义
	 *
	 * @param deployId 流程部署ID act_ge_bytearray 表中 deployment_id值
	 */
	@Override
	public void delete(String deployId) {
		// true 允许级联删除 ,不设置会导致数据库外键关联异常
		repositoryService.deleteDeployment(deployId, true);
	}

}
