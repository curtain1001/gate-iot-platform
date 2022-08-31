package net.pingfang.device.novaled;// package net.pingfang.application.novaled;

//
//import java.io.File;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import javax.swing.JOptionPane;
//
//import net.pingfang.application.novaled.core.NovaTrafficCore;
//import net.pingfang.application.novaled.domain.DeviceSecretParam;
//import net.pingfang.application.novaled.domain.DeviceType;
//import net.pingfang.application.novaled.utils.FileUtils;
//import net.pingfang.application.novaled.utils.ResultCode;
//
//public class Application {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		// testRemoveSetPassword();
//		// testSetPassword();
//
//		// testClientMode();
//		JOptionPane.showMessageDialog(null, "已打开", "测试", JOptionPane.INFORMATION_MESSAGE);
////		testServiceMode();
//		testClientMode();
//		/*
//		 * Thread t1 = new Thread(new Runnable() { public void run() { testCrush(); }
//		 * }); t1.start(); Thread t2 = new Thread(new Runnable() { public void run() {
//		 * testCrush(); } }); t2.start(); Thread t3 = new Thread(new Runnable() { public
//		 * void run() { testCrush(); } }); t3.start(); Thread t4 = new Thread(new
//		 * Runnable() { public void run() { testCrush(); } }); t4.start();
//		 */
//	}
//
//	public static void testCrush() {
//		while (true) {
//			NovaTrafficCore novaTraffic = new NovaTrafficCore("192.168.1.220", 5000);
//			DeviceType mDeviceType = novaTraffic.getDeviceType();
//			String name = novaTraffic.getDeviceName();
//			String tempStr = "获取控制卡状态结果:";
//			String outTest = "";
//			outTest += tempStr + mDeviceType.toString() + "\n";
//			System.out.println(name + tempStr + mDeviceType.toString());
//
//			File tempFile = new File("d:\\jtLog\\" + name + ".log");
//			if (!tempFile.exists()) {
//				FileUtils.createEmptyFile(tempFile);
//			}
//
//			try {
//				FileUtils.writeFileAppend(tempFile, outTest.getBytes(), 0, outTest.getBytes().length);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * 测试加密操作
//	 */
//	public static void testSetPassword() {
//		NovaTrafficCore novaTraffic = new NovaTrafficCore("192.168.0.220", 5000);
//		DeviceSecretParam mDeviceSecretParam = new DeviceSecretParam("123456", true, false, "12345678");
//		int ret = novaTraffic.setDeviceSecretParam(mDeviceSecretParam);
//		System.out.println("设置des加密结果: " + ret);
//		novaTraffic.NovaTrafficSetPassword("12345678");
//		test(novaTraffic);
//	}
//
//	/**
//	 * 移除加密操作
//	 */
//	public static void testRemoveSetPassword() {
//		NovaTrafficCore novaTraffic = new NovaTrafficCore("192.168.0.220", 5000);
//		DeviceSecretParam mDeviceSecretParam = new DeviceSecretParam("123456", false, false, null);
//		int ret = novaTraffic.setDeviceSecretParam(mDeviceSecretParam);
//		System.out.println("设置des加密结果: " + ret);
//		test(novaTraffic);
//	}
//
//	/**
//	 * 此方法本机做客户端连接控制卡5000端口，控制卡需设置服务端模式。
//	 */
//	public static void testClientMode() {
//		NovaTrafficCore novaTraffic = new NovaTrafficCore("192.168.1.220", 5000);
//		test(novaTraffic);
//	}
//
//	/**
//	 * 此方法监听本机5000端口，控制卡需设置客户端模式。
//	 */
//	public static void testServiceMode() {
//		try {
//			@SuppressWarnings("resource")
//			ServerSocket serverSocket = new ServerSocket(5000);
//			while (true) {
//				Socket socket = serverSocket.accept();
//				Thread t1 = new Thread(new Runnable() {
//					public void run() {
//						NovaTrafficCore novaTraffic = new NovaTrafficCore(socket);
//						try {
//							while (true) {
//								test(novaTraffic);
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						} finally {
//							try {
//								socket.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				});
//				t1.start();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 所有测试接口
//	 *
//	 * @param novaTraffic
//	 */
//	@SuppressWarnings({ "deprecation", "unused" })
//	public static void test(NovaTrafficCore novaTraffic) {
//		String name = novaTraffic.getDeviceName();
//
//		String ret = "";
//		String outTest = "";
//
//		ret = ResultCode.getMsg(novaTraffic.setDeviceName(name));
//		String tempStr = "设置设备名称结果:";
//		outTest += tempStr + ret + "\n";
//		System.out.println(name + tempStr + ret);
//
//		tempStr = "发送文件结果:";
//		ret = ResultCode.getMsg(novaTraffic.sendFile(new File("d:\\1.jpg"), "tck.jpg"));
//		outTest += tempStr + ret + "\n";
//		System.out.println(name + tempStr + ret);
//
//		String content = "[all]\r\n" //
//				+ "items=1\r\n" //
//				+ "[item1]\r\n" //
//				+ "param=100,1,1,1,0,5,1,0,1\r\n" //
//				+ "txt1=0,0,3,1616,1,8,0,王超超,32,16,0\r\n" //
//				+ "txtparam1=0,0\r\n"; //
//
//		ret = ResultCode.getMsg(novaTraffic.sendPlayList(1, content));
//		tempStr = "播放play001结果:";
//		outTest += tempStr + ret + "\n";
//		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.sendLocalUpdate(1, content);
////		tempStr = "局部更新索引1结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.removeLocalUpdate(1);
////		tempStr = "移除局部更新结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.setBrightness(130);
////		tempStr = "设置亮度130结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.setPower(true);
////		tempStr = "开屏结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		/*
////		 * ret = novaTraffic.cleanUpFiles(1); tempStr = "清理无效播放文件结果:"; outTest +=
////		 * tempStr + ret +"\n"; System.out.println(name + tempStr +ret);
////		 */
////
////		List<BrightnessItem> brightnessItem = new ArrayList<BrightnessItem>();
////		for (int i = 0; i < 8; i++) {
////			BrightnessItem item = new BrightnessItem(10 * i, 10 * i);
////			brightnessItem.add(item);
////		}
////		ret = novaTraffic.setBrightnessAuto(brightnessItem);
////		tempStr = "设置自动亮度参数结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		List<BrightnessItem> BrightnessItemList = novaTraffic.getBrightnessAuto();
////		tempStr = "获取自动亮度参数结果:";
////		outTest += tempStr + BrightnessItemList.toString() + "\n";
////		System.out.println(name + tempStr + BrightnessItemList.toString());
////
////		ret = novaTraffic.setBoardPowerAndScreenPower(true);
////		tempStr = "控制本板电源和屏幕结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.setBoardPower(true);
////		tempStr = "控制本板电源结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.setMultiFunctionCardPower(0, 0, true);
////		tempStr = "控制多功能卡电源结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		DeviceType mDeviceType = novaTraffic.getDeviceType();
////		tempStr = "获取控制卡状态结果:";
////		outTest += tempStr + mDeviceType.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceType.toString());
////
////		ret = novaTraffic.setTime(new Date());
////		tempStr = "设置时间结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////		// 暂不测试 都会重启或断开连接
////		/*
////		 * ret = novaTraffic.restartDevice(); outTest += "重启结果:" + ret +"\n";
////		 * System.out.println(name + "重启结果:" +ret);
////		 *
////		 * novaTraffic.resetDeviceIpToDefault();
////		 *
////		 * DeviceNetBasicParam mDeviceNetBasicParam = new
////		 * DeviceNetBasicParam("192.168.0.220",5000,
////		 * "255.255.255.0","192.168.0.1","192.168.0.103");
////		 * novaTraffic.setDeviceNetBasicParam(mDeviceNetBasicParam);
////		 */
////
////		String verison = novaTraffic.getDeviceVersion();
////		tempStr = "获取版本结果:";
////		outTest += tempStr + verison + "\n";
////		System.out.println(name + tempStr + verison);
////
////		DeviceSize mDeviceSize = novaTraffic.getDeviceSize();
////		tempStr = "获取设备尺寸结果:";
////		outTest += tempStr + mDeviceSize.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceSize.toString());
////
////		ret = novaTraffic.setDeviceVolume(50);
////		tempStr = "设置声音结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		DeviceMemorySize mDeviceMemorySize = novaTraffic.getDeviceMemorySize();
////		tempStr = "获取设备内存结果:";
////		outTest += tempStr + mDeviceMemorySize.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceMemorySize.toString());
////
////		TimeZoneParam mTimeZoneParam = new TimeZoneParam((long) (new Date().getTime()), "Asia/Shanghai", "GMT+08:00");
////		ret = novaTraffic.setTimeAndTimeZone(mTimeZoneParam);
////		tempStr = "设置时间时区结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		mTimeZoneParam = novaTraffic.getTimeAndTimeZone();
////		tempStr = "获取时间时区结果:";
////		outTest += tempStr + mTimeZoneParam.toString() + "\n";
////		System.out.println(name + tempStr + mTimeZoneParam.toString());
////
////		DeviceNTPParam mDeviceNTPParam = new DeviceNTPParam(false, "");
////		ret = novaTraffic.setDeviceNTPParam(mDeviceNTPParam);
////		tempStr = "设置ntp对时结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		mDeviceNTPParam = novaTraffic.getDeviceNTPParam();
////		tempStr = "获取ntp对时结果:";
////		outTest += tempStr + mDeviceNTPParam.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceNTPParam.toString());
////
////		ret = novaTraffic.setCloseScreenTemperature(80);
////		tempStr = "设置高温关屏温度结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.getCloseScreenTemperature();
////		tempStr = "获取高温关屏温度结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		ret = novaTraffic.setVirtualConnection(false, 50, 1);
////		tempStr = "获取高温关屏温度结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		/*
////		 * ret = novaTraffic.sendFtpDownloadaddress(
////		 * "ftp://user:password@192.168.0.103:21/UPDATE_OS_JT-TAURUSV010803CN0601.nuzip"
////		 * ); tempStr = "ftp发送文件结果:"; outTest += tempStr + ret +"\n";
////		 * System.out.println(name + tempStr +ret);
////		 *
////		 * if(ret == NovaUtils.SUCCESS) { ret =
////		 * novaTraffic.updateFileName("UPDATE_OS_JT-TAURUSV010803CN0601.nuzip"); tempStr
////		 * = "升级结果:"; outTest += tempStr + ret +"\n"; System.out.println(name + tempStr
////		 * +ret); }
////		 */
////
////		// 切换或播放未准备好时可能为空，上面有节目操作，停一秒，当播放出来后再处理
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		DeviceNowPlayItem mDeviceNowPlayItem = novaTraffic.getNowPlayContent();
////		tempStr = "获取当前播放item结果:";
////		outTest += tempStr + mDeviceNowPlayItem.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceNowPlayItem.toString());
////
////		try {
////			Thread.sleep(1000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		DeviceNowPlayList mDeviceNowPlayList = novaTraffic.getNowPlayAllContent();
////		tempStr = "获取当前播放全部结果:";
////		outTest += tempStr + mDeviceNowPlayList.toString() + "\n";
////		System.out.println(name + tempStr + mDeviceNowPlayList.toString());
////
////		ret = novaTraffic.getDeviceScreenshot("d:\\2.jpg");
////		tempStr = "截屏d:\\\\2.jpg结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		String startStr = "2020.1.1 00:00:00";
////		String endStr = "2020.12.31 23:59:59";
////		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
////		sdf.setTimeZone(TimeZone.getDefault());
////		Date start = null;
////		Date end = null;
////		try {
////			List<DevicePowerByTimeParam> mDevicePowerByTimeParamList = new ArrayList<DevicePowerByTimeParam>();
////			start = sdf.parse(startStr);
////			end = sdf.parse(endStr);
////			DevicePowerByTimeParam mDevicePowerByTimeParam = new DevicePowerByTimeParam(start, end);
////			mDevicePowerByTimeParamList.add(mDevicePowerByTimeParam);
////			ret = novaTraffic.setDevicePowerByTimeList(mDevicePowerByTimeParamList);
////			tempStr = "设置定时开屏列表结果:";
////			outTest += tempStr + ret + "\n";
////			System.out.println(name + tempStr + ret);
////		} catch (ParseException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		List<DevicePowerByTimeParam> mDevicePowerByTimeParamList = novaTraffic.getDevicePowerByTimeList();
////		tempStr = "获取定时开屏列表结果:";
////		outTest += tempStr + mDevicePowerByTimeParamList.toString() + "\n";
////		System.out.println(name + tempStr + mDevicePowerByTimeParamList.toString());
////
////		ret = novaTraffic.setPlayByTimeList(null);
////		tempStr = "清理定时开屏列表结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		try {
////			List<PlayByTimeParam> mPlayByTimeParamList = new ArrayList<PlayByTimeParam>();
////			start = sdf.parse(startStr);
////			end = sdf.parse(endStr);
////			PlayByTimeParam mPlayByTimeParam = new PlayByTimeParam(start, end, 1);
////			mPlayByTimeParamList.add(mPlayByTimeParam);
////			ret = novaTraffic.setPlayByTimeList(mPlayByTimeParamList);
////			tempStr = "设置定时播放列表结果:";
////			outTest += tempStr + ret + "\n";
////			System.out.println(name + tempStr + ret);
////		} catch (ParseException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		List<PlayByTimeParam> mPlayByTimeParamList = novaTraffic.getPlayByTimeList();
////		tempStr = "获取定时播放列表结果:";
////		outTest += tempStr + mPlayByTimeParamList.toString() + "\n";
////		System.out.println(name + tempStr + mPlayByTimeParamList.toString());
////
////		ret = novaTraffic.setPlayByTimeList(null);
////		tempStr = "清理定时播放列表结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		try {
////			List<BrightnessByTimeParam> mBrightnessByTimeParamList = new ArrayList<BrightnessByTimeParam>();
////			start = sdf.parse(startStr);
////			end = sdf.parse(endStr);
////			BrightnessByTimeParam mBrightnessByTimeParam = new BrightnessByTimeParam(start, end, 50);
////			mBrightnessByTimeParamList.add(mBrightnessByTimeParam);
////			ret = novaTraffic.setBrightnessByTimeList(mBrightnessByTimeParamList);
////			tempStr = "设置定时亮度列表结果:";
////			outTest += tempStr + ret + "\n";
////			System.out.println(name + tempStr + ret);
////		} catch (ParseException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		List<BrightnessByTimeParam> mBrightnessByTimeParamList = novaTraffic.getBrightnessByTimeList();
////		tempStr = "获取定时亮度列表结果:";
////		outTest += tempStr + mBrightnessByTimeParamList.toString() + "\n";
////		System.out.println(name + tempStr + mBrightnessByTimeParamList.toString());
////
////		ret = novaTraffic.setPlayByTimeList(null);
////		tempStr = "清理定时亮度列表结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		Random r = new Random();
////		ret = novaTraffic.setRelayPowers(r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean());
////		tempStr = "继电器开关测试结果:";
////		outTest += tempStr + ret + "\n";
////		System.out.println(name + tempStr + ret);
////
////		File tempFile = new File("d:\\jtLog\\" + name + ".log");
////		if (!tempFile.exists()) {
////			FileUtils.createEmptyFile(tempFile);
////		}
////
////		try {
////			FileUtils.writeFileAppend(tempFile, outTest.getBytes(), 0, outTest.getBytes().length);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		// JOptionPane.showMessageDialog(null, name +"\n" + outTest, "测试",
//		// JOptionPane.INFORMATION_MESSAGE);
//	}
//
//}
