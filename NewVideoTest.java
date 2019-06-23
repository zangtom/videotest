package com.lenovo.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import org.junit.Test;

import cn.com.magnity.sdk.MagDevice;
import cn.com.magnity.sdk.MagDevice.IFrameCallBack;
import cn.com.magnity.sdk.MagDevice.IReconnectCallBack;
import cn.com.magnity.sdk.MagDevice.IRectROICallBack;
import cn.com.magnity.sdk.types.CameraInfo;
import cn.com.magnity.sdk.types.CorrectionPara;
import cn.com.magnity.sdk.types.DDTPara;
import cn.com.magnity.sdk.types.MDT;
import cn.com.magnity.sdk.types.RectROI;
import cn.com.magnity.sdk.types.RectROIReport;
import cn.com.magnity.sdk.types.RemoteInfo;
import cn.com.magnity.sdk.types.StatisticInfo;

public class NewVideoTest {

	// mine.................................
	MagDevice magDevice;

	// 初始化
//	 @Test
	public void init() {

		System.load("/opt/sotest/libthermogroupsdk4java.so");
		magDevice = new MagDevice();
		 boolean connect = magDevice.connect("192.168.1.207");
		// int方式
//		int i = 825831985;
//		boolean connect = magDevice.connect(i);
		System.out.println("connect:" + connect);
	}

	// 链接设备
	@Test
	public void testConnect() throws UnknownHostException {
		init();

		boolean b1 = magDevice.connect("192.168.1.207");
		System.err.println("connect:" + b1);
		magDevice.disconnect();
		boolean b2 = magDevice.connect("192.168.1.207", 3000);
		System.err.println("connect:" + b2);
		// int
		// int i = IpV4Util.ipToInt("192.168.1.207");
		boolean b3 = magDevice.isConnected();
		System.err.println(b3);
	}

	// play接口带回调
	@Test
	public void playTest1() throws InterruptedException, IOException {
		init();
		magDevice.lock();
		boolean play = magDevice.play(

				new IFrameCallBack() {

					@Override
					public void newFrame(int arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						System.err.println("ok!");
					}
				}, 0, 0, MagDevice.StreamType.StreamTemperature);
		System.out.println(play);
		magDevice.unlock();

		Thread.sleep(3000);
		// 0?,1?2?
		magDevice.setColorPalette(2);
		boolean success = magDevice.saveBMP(1, "/root/img1.bmp");
		System.err.println(success);

	}

	// play接口2
	@Test
	public void testplay() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		magService.lock();
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);

		magService.unlock();

		Thread.sleep(3000);

		magService.setColorPalette(2);
		boolean saveBMP = magService.saveBMP(2, "/root/test16.bmp");
		System.out.println(saveBMP);

		magService.stop();
		// System.in.read();
	}

	// isplay
	@Test
	public void isplaying() {
		init();
		boolean b = magDevice.play(null, 0, 0, 2);
		System.err.println(b);

		boolean playing = magDevice.isPlaying();
		System.err.println(playing);

	}

	// getCameraInfo
	@Test
	public void getCameraInfo() {
		init();
		CameraInfo cameraInfo = new CameraInfo();
		System.err.println("cameraInfo" + cameraInfo);

		boolean b = magDevice.getCameraInfo(cameraInfo);

		System.err.println(cameraInfo.charName);

		System.err.println(b);

	}

	// getremoteinfo
	@Test
	public void getremoteinfo() {
		init();
		RemoteInfo remoteInfo = new RemoteInfo();

		boolean remoteInfoRs = magDevice.getRemoteInfo(remoteInfo);

		remoteInfo.charName = "www";
		// remoteInfo.byteEnlargeY1=2;

		System.err.println("remoteInfo.charName:" + remoteInfo.charName);

		boolean remoteInfob = magDevice.setRemoteInfo(remoteInfo);

		System.err.println("remoteInfo:" + remoteInfob);

		boolean rs = magDevice.getRemoteInfo(remoteInfo);
		System.err.println("rs" + rs);
	}

	// testROi
	@Test
	public void testROi() throws IOException {
		init();
		RectROI rectROI = new RectROI();
		rectROI.x0 = 36;
		rectROI.x1 = 116;
		rectROI.y0 = 20;
		rectROI.y1 = 93;
		rectROI.charROIName = "test";
		rectROI.intAlarmTemp = 60000;
		rectROI.intEmissivity = 100;
		RectROI[] rectROIs = new RectROI[1];
		rectROIs[0] = rectROI;
		magDevice.setRemoteROI(rectROIs);
		magDevice.setRemoteROICallBack(new IRectROICallBack() {

			@Override
			public void rectRoiReport(RectROIReport[] arg0, int arg1) {
				// TODO Auto-generated method stub
				System.out.println(arg0[0].isAlarm);
			}
		});

		System.in.read();
	}

	// getRecentHeartBeat
	@Test
	public void getRecentHeartBeat() {
		init();
		int b = magDevice.getRecentHeartBeat();
		System.err.println("RecentHeartBeat:" + b);

	}

	// setReConnectCallBack

	@Test
	public void setReConnectCallBack() throws IOException {
		init();

		boolean b = magDevice.setReConnectCallBack(new IReconnectCallBack() {
			@Override
			public void reconnect(int arg0, int arg1) {
				// TODO Auto-generated method stub

				System.err.println("ok");
			}
		});
		System.err.println("b:" + b);
		System.in.read();
	}

	// resetCamera
	@Test
	public void resetCamera() {
		init();
		boolean b = magDevice.resetCamera();
		System.err.println("resetCamera:" + b);
	}

	// saveDDT
	@Test
	public void saveDDT() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.setColorPalette(2);
		if (connect) {
			CameraInfo cameraInfo = new CameraInfo();

			magService.getCameraInfo(cameraInfo);

			boolean play = magService.play(null, cameraInfo.intVideoWidth, cameraInfo.intVideoHeight,
					MagDevice.StreamType.StreamTemperature);

			System.out.println("play----" + play);

			boolean playing = magService.isPlaying();

			System.out.println("playing....." + playing);

			System.out.println(cameraInfo.intFPAWidth + "----" + cameraInfo.intFPAHeight);

			int length = 1024 + (cameraInfo.intFPAWidth * cameraInfo.intFPAHeight * 2);

			Thread.sleep(3000);
			magService.lock();

			byte[] buffer = new byte[length];

			int saveDDT2Buffer = magService.saveDDT2Buffer(buffer);

			System.out.println("saveDDT2Buffer....." + saveDDT2Buffer);

			int[] videodata = new int[cameraInfo.intVideoWidth * cameraInfo.intVideoHeight];

			magService.getOutputImageData(videodata);

			BufferedImage bufimg = new BufferedImage(cameraInfo.intVideoWidth, cameraInfo.intVideoHeight,
					BufferedImage.TYPE_INT_RGB);
			bufimg.getRaster().setDataElements(0, 0, cameraInfo.intVideoWidth, cameraInfo.intVideoHeight, videodata);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bufimg, "jpg", out);// png 为要保存的图片格式
			byte[] barray = out.toByteArray();
			System.out.println("length..." + barray.length);

			MDT mdt = new MDT();
			mdt.jpgBuf = barray;
			mdt.jpgSize = barray.length;
			mdt.ddtBuf = buffer;
			mdt.ddtSize = saveDDT2Buffer;
			boolean saveMDT = magService.saveMDT(mdt, "/opt/img/mdt4.jpg");
			System.out.println(saveMDT);

			magService.unlock();
			magService.stop();

		}
	}

	// loadMDT
	@Test
	public void test5() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		if (connect) {
			System.out.println("connect....");
			MDT mdt2 = new MDT();
			boolean loadMDT = magService.loadMDT("/opt/img/mdt4.jpg", mdt2);
			System.out.println(loadMDT);
			System.out.println("length...." + mdt2.ddtSize);
		}

	}

	// getOutputImageData
	@Test
	public void test3() throws InterruptedException, IOException {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		if (connect) {
			CameraInfo cameraInfo = new CameraInfo();

			magService.getCameraInfo(cameraInfo);
			boolean play = magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
			System.out.println("play----" + play);
			int[] videodata = new int[cameraInfo.intVideoWidth * cameraInfo.intVideoHeight];
			Thread.sleep(5000);
			boolean outputVideoData = magService.getOutputImageData(videodata);

			BufferedImage bufimg = new BufferedImage(cameraInfo.intVideoWidth, cameraInfo.intVideoHeight,
					BufferedImage.TYPE_INT_RGB);
			bufimg.getRaster().setDataElements(0, 0, cameraInfo.intVideoWidth, cameraInfo.intVideoHeight, videodata);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bufimg, "jpg", out);// png 为要保存的图片格式
			byte[] barray = out.toByteArray();
			System.out.println("length..." + barray.length);

		}
	}

	// saveDDT
	@Test
	public void test4() throws InterruptedException {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		if (connect) {
			CameraInfo cameraInfo = new CameraInfo();
			magService.setColorPalette(2);
			magService.getCameraInfo(cameraInfo);
			boolean play = magService.play(null, 0, 0, 2);
			System.out.println("play----" + play);
			// magService.lock();
			Thread.sleep(3000);
			boolean saveDDT = magService.saveDDT("/opt/img/2.ddt");
			System.out.println(saveDDT);
			// magService.unlock();
			// magService.stop();
		}
	}

	// getTemperatureProbe
	@Test
	public void test6() throws InterruptedException {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		// boolean connect = magService.connect("192.168.1.207");
		// if(connect) {
		CorrectionPara correctionPara = new CorrectionPara();
		if (!magService.getCorrectionPara(correctionPara)) {
			System.out.println("false");
		}
		DDTPara para = new DDTPara();
		File file = new File("/opt/img/mdt4.jpg");
		boolean loadDDT = magService.loadDDT(file.getAbsolutePath(), 0, 0, null, para);
		System.out.println("loadDDT...." + loadDDT);
		int temp = magService.getTemperatureProbe(83, 65, 1);
		System.out.println(temp);
		int mouseTemp = magService.fixTemperature(temp, correctionPara.fEmissivity, 83, 65);
		System.out.println(mouseTemp);
		// }
	}

	// play
	/*@Test
	public void test7() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		boolean play = magService.play(new FrameCallBackImpl(), 0, 0, MagDevice.StreamType.StreamTemperature);
		System.out.println(play);
		Thread.sleep(5000);
		magService.stop();
		System.out.println("stop");
		System.in.read();
	}*/

	// ptz setPTZCmd
	@Test
	public void test8() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		/*
		 * magService.setPTZCmd(1, 20); Thread.sleep(2000); magService.setPTZCmd(0, 0);
		 */

		magService.setPTZCmd(11, 1);
		// magService.setPTZCmd(9, 1);

		magService.setPTZCmd(1, 50);
		Thread.sleep(3000);
		magService.setPTZCmd(0, 0);

		Thread.sleep(3000);

		magService.setPTZCmd(10, 1);

		System.in.read();
	}

	// queryPTZState
	@Test
	public void test9() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		int queryPTZState = magService.queryPTZState(1);
		// Thread.sleep(3000);
		String lastError = MagDevice.getLastError();
		System.out.println(queryPTZState);
		System.out.println(lastError);
	}

	// connect
	@Test
	public void test10() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		RemoteInfo remoteInfo = new RemoteInfo();
		magService.getRemoteInfo(remoteInfo);
		System.out.println(remoteInfo.dwStaticIp);
	}

	// autoFocus
	@Test
	public void test11() throws IOException {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		boolean autoFocus = magService.autoFocus();
		System.out.println(autoFocus);
		System.in.read();
	}

	// setIsothermalPara
	@Test
	public void test12() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		// magService.setAutoEnlargePara(5,100,100);
		magService.setIsothermalPara(29000, 30000);
		magService.setColorPalette(1);
		// Thread.sleep(3000);
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		boolean saveBMP = magService.saveBMP(0, "/opt/img/test12.bmp");
		System.out.println(saveBMP);

		System.in.read();
	}

	// getElectronicZoom
	@Test
	public void test13() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		int electronicZoom = magService.getElectronicZoom();
		System.out.println(electronicZoom);
	}

	// setVideoContrast
	@Test
	public void test14() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		boolean setVideoContrast = magService.setVideoContrast(3);
		System.out.println(setVideoContrast);
	}

	// setVideoBrightness
	@Test
	public void test15() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		boolean setVideoBrightness = magService.setVideoBrightness(4);
		System.out.println(setVideoBrightness);
	}

	// saveBMP
	@Test
	public void test16() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		System.out.println(connect);
		magService.lock();
		magService.play(null, 50, 100, MagDevice.StreamType.StreamVideo);

		magService.setColorPalette(2);
		magService.unlock();
		// String lastError = MagDevice.getLastError();/
		// System.out.println(lastError);
		Thread.sleep(3000);
		boolean saveBMP = magService.saveBMP(0, "/opt/img/test16_1.bmp");
		System.out.println("saveBMP:" + saveBMP);

		magService.stop();
		System.in.read();
	}

	// getCorrectionPara
	@Test
	public void test17() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		CorrectionPara correctionPara = new CorrectionPara();
		boolean para = magService.getCorrectionPara(correctionPara);
		System.out.println(correctionPara.fEmissivity);
	}

	// getOutputImageData
	@Test
	public void test18() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		magService.setColorPalette(2);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int length = 1024 + (cameraInfo.intFPAWidth * cameraInfo.intFPAHeight * 2);

		boolean play = magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);

		System.out.println("play...." + play);
		Thread.sleep(5000);
		magService.lock();

		byte[] buffer = new byte[length];

		int saveDDT2Buffer = magService.saveDDT2Buffer(buffer);

		System.out.println("saveDDT2Buffer......" + saveDDT2Buffer);

		int[] videodata = new int[cameraInfo.intVideoWidth * cameraInfo.intVideoHeight];

		boolean outputImageData = magService.getOutputImageData(videodata);

		System.out.println("outputImageData......" + outputImageData);

		BufferedImage bufimg = new BufferedImage(cameraInfo.intVideoWidth, cameraInfo.intVideoHeight,
				BufferedImage.TYPE_INT_RGB);
		bufimg.getRaster().setDataElements(0, 0, cameraInfo.intVideoWidth, cameraInfo.intVideoHeight, videodata);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufimg, "jpg", out);// png 为要保存的图片格式
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] barray = out.toByteArray();

		MDT mdt = new MDT();
		mdt.jpgBuf = barray;
		mdt.jpgSize = barray.length;
		mdt.ddtBuf = buffer;
		mdt.ddtSize = saveDDT2Buffer;
		boolean saveMDT = magService.saveMDT(mdt, "/opt/img/test18.jpg");

		System.out.println(saveMDT);
		String lastError = MagDevice.getLastError();
		System.out.println(lastError);

		/*
		 * String base64 = Encodes.ImageToBase64ByLocal("/opt/img/mdt.jpg"); base64 =
		 * "data:image/jpeg;base64,"+base64;
		 */
		magService.unlock();
		System.in.read();
	}

	// getOutputVideoData
	@Test
	public void test19() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		magService.setColorPalette(2);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);

		boolean play = magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);

		System.out.println("play...." + play);
		Thread.sleep(5000);
		magService.lock();

		int[] videodata = new int[cameraInfo.intVideoWidth * cameraInfo.intVideoHeight];

		boolean outputImageData = magService.getOutputVideoData(videodata);

		System.out.println("outputImageData......" + outputImageData);

		BufferedImage bufimg = new BufferedImage(cameraInfo.intVideoWidth, cameraInfo.intVideoHeight,
				BufferedImage.TYPE_INT_RGB);
		bufimg.getRaster().setDataElements(0, 0, cameraInfo.intVideoWidth, cameraInfo.intVideoHeight, videodata);

		try {
			boolean flag = ImageIO.write(bufimg, "jpg", new File("/opt/img/test19.jpg"));// png 为要保存的图片格式
			System.out.println(flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.in.read();
	}

	// getOutputColorBarData
	@Test
	public void test20() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		magService.setColorPalette(2);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);

		boolean play = magService.play(null, 20, 200, MagDevice.StreamType.StreamTemperature);

		System.out.println("play...." + play);
		Thread.sleep(5000);
		magService.lock();

		int[] videodata = new int[20 * 200];

		boolean outputImageData = magService.getOutputColorBarData(videodata);

		System.out.println("outputImageData......" + outputImageData);

		BufferedImage bufimg = new BufferedImage(20, 200, BufferedImage.TYPE_INT_RGB);
		bufimg.getRaster().setDataElements(0, 0, 20, 200, videodata);

		try {
			boolean flag = ImageIO.write(bufimg, "jpg", new File("/opt/img/test20.jpg"));// png 为要保存的图片格式
			System.out.println(flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.in.read();
	}

	// getFrameStatisticInfo
	@Test
	public void test21() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		StatisticInfo statisticInfo = new StatisticInfo();
		boolean frameStatisticInfo = magService.getFrameStatisticInfo(statisticInfo);
		System.out.println(frameStatisticInfo);
		System.out.println(statisticInfo.intMaxTemperature);
		System.out.println(statisticInfo.intMinTemperature);
		System.out.println(statisticInfo.intAveTemperature);
		System.out.println(statisticInfo.intMaxPos);
		System.out.println(statisticInfo.intMinPos);
	}

	// fixTemperature
	@Test
	public void test22() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		int temperatureProbe = magService.getTemperatureProbe(620, 232, 1);
		System.out.println(temperatureProbe);
		CorrectionPara correctionPara = new CorrectionPara();
		boolean para = magService.getCorrectionPara(correctionPara);
		int fixTemperature = magService.fixTemperature(temperatureProbe, correctionPara.fEmissivity, 620, 232);
		System.out.println(fixTemperature);
	}

	// fixTemperature
	@Test
	public void test23() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		int temperatureProbe = magService.getTemperatureProbe(620, 232, 1);
		System.out.println(temperatureProbe);
		CorrectionPara correctionPara = new CorrectionPara();
		boolean para = magService.getCorrectionPara(correctionPara);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int pos = 232 * cameraInfo.intFPAWidth;
		int fixTemperature = magService.fixTemperature(temperatureProbe, correctionPara.fEmissivity, pos);
		System.out.println(fixTemperature);

	}

	// getLineTemperatureInfo
	@Test
	public void test24() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		int[] temp = new int[3];
		boolean lineTemperatureInfo = magService.getLineTemperatureInfo(10, 30, 12, 32, temp);
		System.out.println(lineTemperatureInfo);
		System.out.println(temp[0] + "---" + temp[1] + "---" + temp[2]);
	}

	// getCameraInfo
	@Test
	public void test25() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		int[] temp = new int[5];
		magService.getRectTemperatureInfo(10, 30, 12, 32, temp);
		System.out.println(temp[0] + "---" + temp[1] + "---" + temp[2] + "---" + temp[3] + "---" + temp[4]);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int x = temp[3] / cameraInfo.intFPAWidth;
		System.out.println(x);
	}

	// getEllipseTemperatureInfo
	@Test
	public void test26() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		int[] temp = new int[5];
		boolean ellipseTemperatureInfo = magService.getEllipseTemperatureInfo(10, 50, 60, 70, temp);
		System.out.println(ellipseTemperatureInfo);
		System.out.println(temp[0] + "---" + temp[1] + "---" + temp[2] + "---" + temp[3] + "---" + temp[4]);
	}

	// getRgnTemperatureInfo
	@Test
	public void test27() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int[] pos = new int[] { 10 + 10 * cameraInfo.intFPAWidth, 10 + 11 * cameraInfo.intFPAWidth,
				10 + 12 * cameraInfo.intFPAWidth, 11 + 12 * cameraInfo.intFPAWidth };
		int[] temp = new int[5];
		boolean rgnTemperatureInfo = magService.getRgnTemperatureInfo(pos, temp);
		System.out.println(rgnTemperatureInfo);
		System.out.println(temp[0] + "---" + temp[1] + "---" + temp[2] + "---" + temp[3] + "---" + temp[4]);
	}

	// saveDDT
	@Test
	public void test28() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		boolean saveDDT = magService.saveDDT("/opt/sotest/test28.ddt");
		System.out.println(saveDDT);

	}

	// loadDDT
	@Test
	public void test29() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		// boolean connect = magService.connect("192.168.1.207");
		DDTPara para = new DDTPara();
		File file = new File("/opt/sotest/test28.ddt");
		if (file.exists()) {
			boolean loadDDT = magService.loadDDT(file.getAbsolutePath(), 0, 0, null, para);
			System.out.println(loadDDT);
			String lastError = MagDevice.getLastError();
			System.out.println(lastError);
			System.out.println(para.fpaWidth);
			System.out.println(para.fpaHeight);
			System.out.println(para.fileTime);
			System.out.println(para.serialNumber);
			System.out.println(para.cameraType);
			System.out.println(para.cameraName);
			System.out.println(para.airTemp);
			magService.unloadDDT();
		}
	}

	// saveDDT2Buffer
	@Test
	public void test30() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");

		magService.setColorPalette(2);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int length = 1024 + (cameraInfo.intFPAWidth * cameraInfo.intFPAHeight * 2);

		boolean play = magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);

		System.out.println("play...." + play);
		Thread.sleep(5000);
		magService.lock();

		byte[] buffer = new byte[length];

		int saveDDT2Buffer = magService.saveDDT2Buffer(buffer);

		MagDevice magDevice = new MagDevice();
		System.out.println("saveDDT2Buffer......" + saveDDT2Buffer);
		DDTPara para = new DDTPara();
		boolean loadBufferedDDT = magDevice.loadBufferedDDT(0, 0, buffer, null, para);
		System.out.println(loadBufferedDDT);
		magService.unloadDDT();
	}

	// loadMDT
	@Test
	public void test31() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		MDT mdt = new MDT();
		boolean loadMDT = magService.loadMDT("/opt/img/test18.jpg", mdt);
		System.out.println(loadMDT);
		System.out.println(mdt.jpgSize);
	}

	// transferPulseImage
	@Test
	public void test32() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		System.out.println("connect......" + connect);
		boolean playPulse = magService.playPulse(null, 4, 4, MagDevice.StreamType.StreamTemperature);
		System.out.println("playPulse......" + playPulse);
		Thread.sleep(3000);
		boolean transferPulseImage = magService.transferPulseImage();
		System.out.println(transferPulseImage);
		Thread.sleep(3000);
		boolean saveBMP = magService.saveBMP(0, "/opt/img/test32.bmp");
		String lastError = MagDevice.getLastError();
		System.out.println(lastError);
		System.out.println(saveBMP);

		magService.stop();
	}

	// setCorrectionPara
	@Test
	public void test33() {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		CorrectionPara correctionPara = new CorrectionPara();
		magService.getCorrectionPara(correctionPara);
		System.out.println(correctionPara.fEmissivity);
		correctionPara.fEmissivity = (float) 1.0;
		magService.setCorrectionPara(correctionPara, true);
		magService.getCorrectionPara(correctionPara);
		System.out.println(correctionPara.fEmissivity);
	}

	// getTemperatureData
	@Test
	public void test34() throws Exception {
		System.load("/opt/sotest/libthermogroupsdk4java.so");
		MagDevice magService = new MagDevice();
		boolean connect = magService.connect("192.168.1.207");
		magService.play(null, 0, 0, MagDevice.StreamType.StreamTemperature);
		Thread.sleep(3000);
		CameraInfo cameraInfo = new CameraInfo();
		magService.getCameraInfo(cameraInfo);
		int[] temp = new int[cameraInfo.intFPAHeight * cameraInfo.intFPAWidth];
		boolean temperatureData = magService.getTemperatureData(temp);
		System.out.println(temperatureData);
		System.out.println(temp[1]);

	}
}
