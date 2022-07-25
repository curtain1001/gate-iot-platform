package net.pingfang.flowable.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.flowable.domain.vo.FlowTaskVo;
import net.pingfang.flowable.service.IFlowTaskService;

/**
 * <p>
 * 工作流任务管理
 * <p>
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Slf4j
@RestController
@RequestMapping("/flowable/task")
public class FlowTaskController {

	@Autowired
	private IFlowTaskService flowTaskService;

	/**
	 * 我发起的流程
	 *
	 * @param pageNum  当前页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping(value = "/myProcess")
	public AjaxResult myProcess(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
		return flowTaskService.myProcess(pageNum, pageSize);
	}

	/**
	 * 取消申请
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/stopProcess")
	public AjaxResult stopProcess(@RequestBody FlowTaskVo flowTaskVo) {
		return flowTaskService.stopProcess(flowTaskVo);
	}

	/**
	 * 撤回流程
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/revokeProcess")
	public AjaxResult revokeProcess(@RequestBody FlowTaskVo flowTaskVo) {
		return flowTaskService.revokeProcess(flowTaskVo);
	}

	/**
	 * 获取待办列表
	 *
	 * @param pageNum  当前页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping(value = "/todoList")
	public AjaxResult todoList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
		return flowTaskService.todoList(pageNum, pageSize);
	}

	/**
	 * 获取已办任务
	 *
	 * @param pageNum  当前页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping(value = "/finishedList")
	public AjaxResult finishedList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
		return flowTaskService.finishedList(pageNum, pageSize);
	}

	/**
	 * 流程历史流转记录
	 *
	 * @param procInsId
	 * @param deployId
	 * @return
	 */
	@GetMapping(value = "/flowRecord")
	public AjaxResult flowRecord(String procInsId, String deployId) {
		return flowTaskService.flowRecord(procInsId, deployId);
	}

	/**
	 * 获取流程变量
	 *
	 * @param taskId 流程任务Id
	 * @return
	 */
	@GetMapping(value = "/processVariables/{taskId}")
	public AjaxResult processVariables(@PathVariable(value = "taskId") String taskId) {
		return flowTaskService.processVariables(taskId);
	}

	/**
	 * 审批任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/complete")
	public AjaxResult complete(@RequestBody FlowTaskVo flowTaskVo) {
		return flowTaskService.complete(flowTaskVo);
	}

	/**
	 * 驳回任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/reject")
	public AjaxResult taskReject(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.taskReject(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 退回任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/return")
	public AjaxResult taskReturn(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.taskReturn(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 获取所有可回退的节点
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/returnList")
	public AjaxResult findReturnTaskList(@RequestBody FlowTaskVo flowTaskVo) {
		return flowTaskService.findReturnTaskList(flowTaskVo);
	}

	/**
	 * 删除任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public AjaxResult delete(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.deleteTask(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 认领/签收任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/claim")
	public AjaxResult claim(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.claim(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 取消认领/签收任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/unClaim")
	public AjaxResult unClaim(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.unClaim(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 委派任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/delegate")
	public AjaxResult delegate(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.delegateTask(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 转办任务
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/assign")
	public AjaxResult assign(@RequestBody FlowTaskVo flowTaskVo) {
		flowTaskService.assignTask(flowTaskVo);
		return AjaxResult.success();
	}

	/**
	 * 获取下一节点
	 *
	 * @param flowTaskVo
	 * @return
	 */
	@PostMapping(value = "/nextFlowNode")
	public AjaxResult getNextFlowNode(@RequestBody FlowTaskVo flowTaskVo) {
		return flowTaskService.getNextFlowNode(flowTaskVo);
	}

	/**
	 * 生成流程图
	 *
	 * @param processId 任务ID
	 */
	@RequestMapping("/diagram/{processId}")
	public void genProcessDiagram(HttpServletResponse response, @PathVariable("processId") String processId) {
		InputStream inputStream = flowTaskService.diagram(processId);
		OutputStream os = null;
		BufferedImage image = null;
		try {
			image = ImageIO.read(inputStream);
			response.setContentType("image/png");
			os = response.getOutputStream();
			if (image != null) {
				ImageIO.write(image, "png", os);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.flush();
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成流程图
	 *
	 * @param procInsId 任务ID
	 */
	@RequestMapping("/flowViewer/{procInsId}")
	public AjaxResult getFlowViewer(@PathVariable("procInsId") String procInsId) {
		return flowTaskService.getFlowViewer(procInsId);
	}
}
