//package net.pingfang.business.controller;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//
//import net.pingfang.business.domain.BtpFlow;
//import net.pingfang.business.service.IBtpGateService;
//import net.pingfang.common.core.controller.BaseController;
//import net.pingfang.common.core.domain.AjaxResult;
//
///**
// * <p>
// *
// * </p>
// *
// * @author 王超
// * @since 2022-10-18 14:03
// */
//@RestController
//@RequestMapping("business/gate-service")
//public class BtpGateServiceController extends BaseController {
//	@Resource
//	IBtpGateService gateService;
//
//	/**
//	 * 分页列表
//	 *
//	 * @param flow 流程信息
//	 * @return 分页数据
//	 */
//	@PreAuthorize("@ss.hasPermi('business:gate-service:list')")
//	@GetMapping("/list")
//	public AjaxResult list(BtpFlow flow) {
//		LambdaQueryWrapper<BtpFlow> queryWrapper = Wrappers.lambdaQuery();
//		queryWrapper.like(StringUtils.checkValNotNull(flow.getFlowId()), BtpFlow::getFlowId, flow.getFlowId());
//		queryWrapper.like(StringUtils.checkValNotNull(flow.getFlowName()), BtpFlow::getFlowName, flow.getFlowName());
//		queryWrapper.like(StringUtils.checkValNotNull(flow.getLaneId()), BtpFlow::getLaneId, flow.getLaneId());
//		queryWrapper.orderByDesc(BtpFlow::getCreateTime);
//		List<BtpFlow> list = flowService.list(queryWrapper);
//		return getDataTable(list);
//	}
//}
