package net.pingfang.business.repository;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.pingfang.business.values.ArriveLeave;
import net.pingfang.business.values.CmdInfo;
import net.pingfang.business.values.EquipmentStateMsg;
import net.pingfang.business.values.OcrWork;
import net.pingfang.common.core.domain.AjaxResult;
import net.pingfang.common.utils.JsonUtils;
import net.pingfang.common.utils.http.HttpUtils2;

/**
 * <p>
 *
 * </p>
 *
 * @author 王超
 * @since 2022-12-21 0:18
 */
@Repository
@Slf4j
public class CloudGateRepository {
	@Value("${cloud-gate.host}")
	String host;

	@Value("${cloud-gate.arriveOrLeavePath}")
	String arriveOrLeavePath;

	@Value("${cloud-gate.cmdPath}")
	String cmdPath;

	@Value("${cloud-gate.ocrWorkingPath}")
	String ocrWorkingPath;

	@Value("${cloud-gate.webSubmitWorkingPath}")
	String webSubmitWorkingPath;

	@Value("${cloud-gate.equipmentStateMsgPath}")
	String equipmentStateMsgPath;

	/**
	 * 车辆到达或离开报文
	 *
	 * @param arriveLeave 流程开始报文
	 * @return 发送情况
	 */
	public AjaxResult arriveOrLeave(ArriveLeave arriveLeave) {
		Map<String, String> hears = Maps.newHashMap();
		hears.put("content-type", "application/json;charset=UTF-8");
		try {
			HttpResponse response = HttpUtils2.doPost(host, arriveOrLeavePath, hears, null,
					JsonUtils.toJsonString(arriveLeave));
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 200) {
				return JsonUtils.toObject(res, AjaxResult.class);
			} else {
				return AjaxResult.error(response.getStatusLine().getStatusCode(), res);
			}
		} catch (Exception e) {
			log.error("调用车辆到达或离开接口失败：", e);
			return AjaxResult.error(500, e.getMessage());
		}
	}

	/**
	 * 命令报文
	 *
	 * @param cmdInfo 命令报文
	 * @return 发送情况
	 */
	public AjaxResult cmdInfo(CmdInfo cmdInfo) {
		Map<String, String> hears = Maps.newHashMap();
		hears.put("content-type", "application/json;charset=UTF-8");
		try {
			HttpResponse response = HttpUtils2.doPost(host, cmdPath, hears, null, JsonUtils.toJsonString(cmdInfo));
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 200) {
				return JsonUtils.toObject(res, AjaxResult.class);
			} else {
				return AjaxResult.error(response.getStatusLine().getStatusCode(), res);
			}
		} catch (Exception e) {
			log.error("调用命令报文失败：", e);
			return AjaxResult.error(500, e.getMessage());
		}
	}

	/**
	 *
	 * 新增作业识别报文
	 *
	 * @param ocrWork 作业识别报文
	 * @return 发送情况
	 */
	public AjaxResult ocrWorking(OcrWork ocrWork) {
		Map<String, String> hears = Maps.newHashMap();
		hears.put("content-type", "application/json;charset=UTF-8");
		try {
			HttpResponse response = HttpUtils2.doPost(host, ocrWorkingPath, hears, null,
					JsonUtils.toJsonString(ocrWork));
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 200) {
				return JsonUtils.toObject(res, AjaxResult.class);
			} else {
				return AjaxResult.error(response.getStatusLine().getStatusCode(), res);
			}
		} catch (Exception e) {
			log.error("调用命令报文失败：", e);
			return AjaxResult.error(500, e.getMessage());
		}
	}

	/**
	 *
	 *
	 * web提交作业报文
	 *
	 * @param ocrWork web提交作业报文
	 * @return 发送情况
	 */
	public AjaxResult webSubmitWorking(OcrWork ocrWork) {
		Map<String, String> hears = Maps.newHashMap();
		hears.put("content-type", "application/json;charset=UTF-8");
		try {
			HttpResponse response = HttpUtils2.doPost(host, webSubmitWorkingPath, hears, null,
					JsonUtils.toJsonString(ocrWork));
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 200) {
				return JsonUtils.toObject(res, AjaxResult.class);
			} else {
				return AjaxResult.error(response.getStatusLine().getStatusCode(), res);
			}
		} catch (Exception e) {
			log.error("调用命令报文失败：", e);
			return AjaxResult.error(500, e.getMessage());
		}

	}

	/**
	 * /gateControl 命令报文
	 */
	public AjaxResult gateControl(CmdInfo cmdInfo) {
		return AjaxResult.success();
	}

	/**
	 * /equipmentStateMsg 新增硬件状态
	 *
	 * @param { "cpuDescribe": "i7-7500U 四核", "cpuUse": "40%", "devicesList": [ {
	 *          "activated": true, "deviceIp": "192.168.1.2", "deviceName": "车牌相机",
	 *          "deviceState": "正常", "deviceType": "硬件", "message": "相机不抓拍" } ],
	 *          "diskDescribe": "1T", "diskUse": "30%", "laneNo": "W101",
	 *          "memoryDescribe": "32G", "memoryUse": "40%", "messageType":
	 *          "equipmentState", "serverIp": "192.168.1.39", "serverName": "应用服务器",
	 *          "updateTime": "2022-04-24 15:35:21" }
	 */
	/**
	 * 新增硬件状态
	 *
	 * @param stateMsg 硬件状态信息
	 * @return 发送情况
	 */
	public AjaxResult addStateMsg(EquipmentStateMsg stateMsg) {
		Map<String, String> hears = Maps.newHashMap();
		hears.put("content-type", "application/json;charset=UTF-8");
		try {
			HttpResponse response = HttpUtils2.doPost(host, equipmentStateMsgPath, hears, null,
					JsonUtils.toJsonString(stateMsg));
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 200) {
				return JsonUtils.toObject(res, AjaxResult.class);
			} else {
				return AjaxResult.error(response.getStatusLine().getStatusCode(), res);
			}
		} catch (Exception e) {
			log.error("调用命令报文失败：", e);
			return AjaxResult.error(500, e.getMessage());
		}
	}

	/**
	 * /listEquipmentByLaneNos/{laneNos} 通过通道号查询硬件状态信息
	 *
	 * @return { "code": 0, "data": [ { "cpuDescribe": "i7-7500U 四核", "cpuUse":
	 *         "40%", "devicesList": [ { "activated": true, "deviceIp":
	 *         "192.168.1.2", "deviceName": "车牌相机", "deviceState": "正常",
	 *         "deviceType": "硬件", "message": "相机不抓拍" } ], "diskDescribe": "1T",
	 *         "diskUse": "30%", "laneNo": "W101", "memoryDescribe": "32G",
	 *         "memoryUse": "40%", "messageType": "equipmentState", "serverIp":
	 *         "192.168.1.39", "serverName": "应用服务器", "updateTime": "2022-04-24
	 *         15:35:21" } ], "msg": "string" }
	 */

}
