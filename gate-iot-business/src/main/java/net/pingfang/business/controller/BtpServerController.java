package net.pingfang.business.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import net.pingfang.business.domain.BtpServer;
import net.pingfang.business.events.ServerNetworkDeleteEvent;
import net.pingfang.business.service.IBtpInstructionService;
import net.pingfang.business.service.IBtpServerService;
import net.pingfang.common.annotation.Log;
import net.pingfang.common.core.controller.BaseController;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.core.page.TableDataInfo;
import net.pingfang.common.enums.BusinessType;
import net.pingfang.common.event.EventBusCenter;
import net.pingfang.iot.common.product.ProductSupports;
import net.pingfang.servicecomponent.core.ServerManager;
import net.pingfang.servicecomponent.core.ServerOperator;

/**
 * @author 王超
 * @description 服务管理
 * @date 2022-06-29 23:28
 */
@RestController
@RequestMapping("business/server")
public class BtpServerController extends BaseController {

	@Resource
	IBtpServerService btpServerService;
	@Resource
	IBtpInstructionService instructionService;
	@Resource
	ServerManager serverManager;
	@Resource
	EventBusCenter busCenter;

	@GetMapping("/product/list")
	public AjaxResult getServerProduct() {
		return AjaxResult.success(ProductSupports.getServerSupports());
	}

	/**
	 * 分页列表
	 *
	 * @param server 通道信息
	 * @return 分页数据
	 */
	@PreAuthorize("@ss.hasPermi('business:server:list')")
	@GetMapping("/list")
	public TableDataInfo list(BtpServer server) {
		startPage();
		LambdaQueryWrapper<BtpServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.like(StringUtils.checkValNotNull(server.getServerId()), BtpServer::getServerId,
				server.getServerId());
		queryWrapper.like(StringUtils.checkValNotNull(server.getServerName()), BtpServer::getServerName,
				server.getServerName());
		queryWrapper.like(StringUtils.checkValNotNull(server.getLaneId()), BtpServer::getLaneId, server.getLaneId());
		queryWrapper.like(StringUtils.checkValNotNull(server.getProduct()), BtpServer::getProduct, server.getProduct());
		queryWrapper.orderByDesc(BtpServer::getCreateTime);
		List<BtpServer> list = btpServerService.list(queryWrapper);
		TableDataInfo info = getDataTable(list);
		info.setRows(info.getRows().stream().peek(x -> {
			((BtpServer) x).setInstructions(
					instructionService.getInstructions(ProductSupports.getSupport(((BtpServer) x).getProduct())));
		}).collect(Collectors.toList()));
		return info;
	}

	/**
	 * 获取该车道所有服务
	 *
	 * @return 所有服务
	 */
	@GetMapping("/all/{laneId}")
	public AjaxResult getServers(@PathVariable("laneId") Long laneId) {
		LambdaQueryWrapper<BtpServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpServer::getLaneId, laneId);
		List<BtpServer> list = btpServerService.list(queryWrapper);
		list = list.stream().peek(x -> {
			x.setInstructions(
					instructionService.getInstructions(ProductSupports.getSupport(((BtpServer) x).getProduct())));
		}).collect(Collectors.toList());
		return AjaxResult.success(list);
	}

	/**
	 * 根据id查询
	 */
	@PreAuthorize("@ss.hasPermi('business:server:query')")
	@GetMapping(value = "/{id}")
	public AjaxResult load(@PathVariable Long id) {
		return AjaxResult.success(btpServerService.getById(id));
	}

	/**
	 * 根据服务号查询
	 */
	@GetMapping(value = "server-id/{serverId}")
	public AjaxResult getServer(@PathVariable String serverId) {
		LambdaQueryWrapper<BtpServer> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(BtpServer::getServerId, serverId);
		return AjaxResult.success(btpServerService.getOne(queryWrapper));
	}

	/**
	 * 根据服务名查询
	 */
	@GetMapping(value = "server-name/{serverName}")
	public AjaxResult findServerByName(@PathVariable String serverName) {
		LambdaQueryWrapper<BtpServer> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpServer::getServerName, serverName);
		return AjaxResult.success(btpServerService.getOne(wrapper));
	}
	// TODO: 2022/8/1 同一车道允许存在多个服务
	// /**
	// * 根据通道号查询
	// */
	// @GetMapping(value = "/lane/{laneId}")
	// public AjaxResult getLaneNo(@PathVariable String laneId) {
	// LambdaQueryWrapper<BtpServer> wrapper = Wrappers.lambdaQuery();
	// wrapper.eq(BtpServer::getLaneId, laneId);
	// return AjaxResult.success(btpServerService.getOne(wrapper));
	// }

	/**
	 * 新增通道信息
	 */
	@PreAuthorize("@ss.hasPermi('business:server:add')")
	@Log(title = "服务管理", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody BtpServer server) {
		LambdaQueryWrapper<BtpServer> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpServer::getServerId, server.getServerId());
		if (btpServerService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务'" + server.getServerId() + "'失败，服务号已存在");
		}
		wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpServer::getServerName, server.getServerName());
		wrapper.eq(StringUtils.checkValNotNull(server.getLaneId()), BtpServer::getLaneId, server.getLaneId());
		if (btpServerService.count(wrapper) > 0) {
			return AjaxResult.error("新增服务'" + server.getServerName() + "'失败，服务名称已存在");
		}
		// 默认初始化服务为未启动
		server.setEnabled(1);
		server.setCreateBy(getUsername());
		server.setCreateTime(new Date());
		boolean result = btpServerService.save(server);
		if (result) {
			return AjaxResult.success();
		} else {
			return AjaxResult.error();
		}
	}

	/**
	 * 修改参数配置
	 */
	@PreAuthorize("@ss.hasPermi('business:server:edit')")
	@Log(title = "服务管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody BtpServer server) {
		LambdaQueryWrapper<BtpServer> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(BtpServer::getServerName, server.getServerName());
		wrapper.eq(BtpServer::getLaneId, server.getServerId());
		BtpServer btpServer = btpServerService.getOne(wrapper);
		if (btpServer != null && !btpServer.getId().equals(server.getId())) {
			return AjaxResult.error("修改服务'" + server.getServerName() + "'失败，服务名已存在");
		}
		server.setUpdateBy(getUsername());
		server.setUpdateTime(new Date());
		boolean result = btpServerService.updateById(server);
		if (result) {
			return AjaxResult.success();
		} else {
			return AjaxResult.error();
		}
	}

	/**
	 * 删除服务
	 */
	@PreAuthorize("@ss.hasPermi('business:server:remove')")
	@Log(title = "服务管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
	public AjaxResult remove(@PathVariable Long[] ids) {
		Arrays.stream(ids).forEach(x -> {
			BtpServer btpServer = btpServerService.getById(x);
			if (btpServer != null) {
				serverManager.shutdown(btpServer.getLaneId(), btpServer.getServerId());
				btpServerService.removeById(x);
				busCenter.postSync(ServerNetworkDeleteEvent.builder() //
						.id(btpServer.getServerId()) //
						.build());
			}
		});
		return AjaxResult.success();
	}

	/**
	 * 开启服务
	 */
	@PreAuthorize("@ss.hasPermi('business:server:open')")
	@Log(title = "服务管理", businessType = BusinessType.UPDATE)
	@PutMapping("{id}/{status:(?:on|off)}")
	public AjaxResult openServer(@Validated @PathVariable Long id, @Validated @PathVariable String status) {
		BtpServer server = btpServerService.getById(id);
		if (server == null) {
			return AjaxResult.error("服务不存在！");
		}
		if ("on".equals(status)) {
			server.setEnabled(0);
			btpServerService.updateById(server);
			ServerOperator serverOperator = serverManager.create(server.getLaneId(), server.getServerId(),
					ProductSupports.getSupport(server.getProduct()));
			if (serverOperator != null) {
				return AjaxResult.success("开启服务成功");
			}
		}
		if ("off".equals(status)) {
			serverManager.shutdown(server.getLaneId(), server.getServerId());
			server.setEnabled(1);
			btpServerService.updateById(server);
			return AjaxResult.success("关闭服务成功");
		}
		return AjaxResult.error("on".equals(status) ? "开启服务失败" : "关闭服务失败");
	}

}
