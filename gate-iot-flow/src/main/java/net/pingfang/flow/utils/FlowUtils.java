package net.pingfang.flow.utils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import javax.script.Bindings;
import javax.script.ScriptException;

import org.apache.commons.compress.utils.Lists;

import com.fasterxml.jackson.databind.JsonNode;

import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import net.pingfang.flow.domain.FlowNode;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-08-09 17:16
 */
public class FlowUtils {
	public static NashornSandbox sandbox;

	static {
		NashornSandbox sandbox = NashornSandboxes.create();
		sandbox.setMaxCPUTime(100);// 设置脚本执行允许的最大CPU时间（以毫秒为单位），超过则会报异常,防止死循环脚本
		sandbox.setMaxMemory(1024 * 1024); // 设置JS执行程序线程可以分配的最大内存（以字节为单位），超过会报ScriptMemoryAbuseException错误
		sandbox.allowNoBraces(false); // 是否允许使用大括号
		sandbox.allowLoadFunctions(true); // 是否允许nashorn加载全局函数
		sandbox.setMaxPreparedStatements(30); // because preparing scripts for execution is expensive //
		// LRU初缓存的初始化大小，默认为0
		sandbox.setExecutor(Executors.newSingleThreadExecutor());// 指定执行程序服务，该服务用于在CPU时间运行脚本
	}

	public static Object execJs(String js, JsonNode jsonNode) throws ScriptException {
		Bindings bindings = sandbox.createBindings();
		bindings.put("data", jsonNode);
		return sandbox.eval(js, bindings);
	}

	public static void main(String[] args) {
		List<FlowNode> flowNodes = Lists.newArrayList();
		flowNodes.add(FlowNode.builder().nodeName("1111").build());
		flowNodes.add(FlowNode.builder().nodeName("2222").build());
		flowNodes.add(FlowNode.builder().nodeName("3333").build());
		flowNodes.add(FlowNode.builder().nodeName("4444").build());
		Iterator<FlowNode> iterator = flowNodes.listIterator();
		while (iterator.hasNext()) {
			FlowNode node = iterator.next();
			System.out.println(node.getNodeName());
			flowNodes.add(FlowNode.builder().nodeName("5555").build());
		}
	}

}
