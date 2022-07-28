package net.pingfang.flow.service;

/**
 * 流程接口
 *
 * @author wangchao
 * @since 2022-07-26
 */
public interface IFlowService {

	/**
	 * success
	 *
	 * @param taskCode 任务实例
	 * @param note     备注
	 */
	void success(String taskCode, String note);

	/**
	 * 不同意
	 *
	 * @param taskCode 任务实例
	 * @param note     备注
	 */
	void fail(String taskCode, String note);

}
