package net.pingfang.flow.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.pingfang.flow.domain.FlowDeployment;

/**
 * <p>
 * 流程定义表 服务实现类
 * </p>
 *
 * @author wangchao
 * @since 2022-07-26
 */
@Service
public class FlowDefinitionServiceImpl extends ServiceImpl<FlowDefinitionMapper, FlowDeployment>
		implements IService<FlowDeployment> {

}
