//package net.pingfang.flow.core;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import com.google.common.eventbus.Subscribe;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import net.pingfang.device.core.event.MessageUpEvent;
//import net.pingfang.iot.common.instruction.Instruction;
//
///**
// * @author 王超
// * @description TODO
// * @date 2022-07-26 16:39
// */
//@AllArgsConstructor
//@Slf4j
////@Component
//public class FlowEngine {
//	/**
//	 * 当前状态
//	 */
////	State currentState;
//	/**
//	 * 当前指令
//	 */
//	Instruction currentInstruction;
//
//	final Map<Long, Process> processStore = new ConcurrentHashMap<>();
//
//	final
//
//	public Map<Long, Process> deploy(Long laneId, Process process) {
//		this.processStore.put(laneId, process);
//		return processStore;
//	}
//
//	public void execute() {
//
//	}
//
//	@Subscribe
//	public void on(MessageUpEvent event) {
//		log.info("设备：{}；指令：{}；", event.getDeviceId(), event.getInstruction().getValue());
//
//	}
//
//	/**
//	 * 流程开始
//	 */
//	public void start() {
//
//	}
//
//	/**
//	 * 流程驱动
//	 */
//	public void device() {
//
//	}
//
//	/**
//	 * 结束流程
//	 */
//	public void end() {
//
//	}
//}
