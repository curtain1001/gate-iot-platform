package net.pingfang.device.licenseplate.values;

import net.sdk.bean.Data_E_ReturnCode.E_ReturnCode;

public class StatusCode {
	public static String getStatusCode(int tmp, String interfaceName) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("执行接口");
		sbf.append(interfaceName);
		sbf.append("完毕,返回值：");

		if (tmp == E_ReturnCode.DC_UNDEFINED_ERROR.getValue()) {
			sbf.append("未知错误发生");
		} else
		/** 正常 0 */
		if (tmp == E_ReturnCode.DC_NO_ERROR.getValue()) {
			sbf.append("状态正常");
		} else
		/** 无效的句柄 1 */
		if (tmp == E_ReturnCode.DC_HANDLE_INVALID.getValue()) {
			sbf.append("无效的句柄,请先添加和连接相机");
		} else
		/** 连接失败 2 */
		if (tmp == E_ReturnCode.DC_CONN_FAIL.getValue()) {
			sbf.append("连接失败");
		} else
		/** 对象忙 3 */
		if (tmp == E_ReturnCode.DC_OBJ_BUSY.getValue()) {
			sbf.append("对象忙");
		} else
		/** 对象不存在 4 */
		if (tmp == E_ReturnCode.DC_OBJ_UNEXIST.getValue()) {
			sbf.append("相机不存在,请先添加和连接相机");
		} else
		/** 命令无效 5 */
		if (tmp == E_ReturnCode.DC_CMD_INVALID.getValue()) {
			sbf.append("命令无效");
		} else
		/** 参数无效 6 */
		if (tmp == E_ReturnCode.DC_PARA_INVALID.getValue()) {
			sbf.append("参数无效");
		} else
		/** 请求超时 7 */
		if (tmp == E_ReturnCode.DC_REQ_TIMEOUT.getValue()) {
			sbf.append("请求超时");
		} else
		/** 内存申请失败 8 */
		if (tmp == E_ReturnCode.DC_MEMORY_LACK.getValue()) {
			sbf.append("内存申请失败");
		} else
		/** 数据发送失败 9 */
		if (tmp == E_ReturnCode.DC_SEND_FAIL.getValue()) {
			sbf.append("数据发送失败");
		} else
		/** 数据接收失败 10 */
		if (tmp == E_ReturnCode.DC_RECV_FAIL.getValue()) {
			sbf.append("数据接收失败");
		} else
		/** 操作失败 11 */
		if (tmp == E_ReturnCode.DC_OPT_FAIL.getValue()) {
			sbf.append("操作失败");
		} else
		/** 没有触发连接 12 */
		if (tmp == E_ReturnCode.DC_NOT_CONN.getValue()) {
			sbf.append("没有触发连接,请先连接相机");
		} else
		/** 超出相机最大连接数量 13 */
		if (tmp == E_ReturnCode.DC_BEYOND_MAX_CLIENT.getValue()) {
			sbf.append("超出相机最大连接数量");
		} else
		/** 连接鉴权 18 */
		if (tmp == E_ReturnCode.DC_CONNECT_AUTH.getValue()) {
			sbf.append("连接鉴权");
		} else
		/** 用户不存在 19 */
		if (tmp == E_ReturnCode.DC_USER_NOT_EXIST.getValue()) {
			sbf.append("用户不存在");
		} else
		/** 密码错误 20 */
		if (tmp == E_ReturnCode.DC_PASSWD_ERROR.getValue()) {
			sbf.append("密码错误");
		}
		return sbf.toString();
	}
}
