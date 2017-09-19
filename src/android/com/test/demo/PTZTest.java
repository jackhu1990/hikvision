package com.test.demo;
import com.hikvision.netsdk.*;
public class PTZTest {
	public static void Test_PTZControl(int iPreviewID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZControl(iPreviewID, PTZCommand.PAN_LEFT, 0))
		{
			System.out.println("PTZControl  PAN_LEFT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZControl  PAN_LEFT 0 succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_PTZControl(iPreviewID, PTZCommand.PAN_LEFT, 1))
		{
			System.out.println("PTZControl  PAN_LEFT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZControl  PAN_LEFT 1 succ");
		}
	}
	public static void Test_PTZControlWithSpeed(int iPreviewID)
	{
		if(! HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(iPreviewID,PTZCommand.PAN_RIGHT, 0, 4))
		{
			System.out.println("PTZControlWithSpeed  PAN_RIGHT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZControlWithSpeed  PAN_RIGHT 0 succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(! HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed(iPreviewID,PTZCommand.PAN_RIGHT, 1, 4))
		{
			System.out.println("PTZControlWithSpeed  PAN_RIGHT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZControlWithSpeed  PAN_RIGHT 1 succ");
		}
	}
	public static void Test_PTZPreset(int iPreviewID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZPreset(iPreviewID, PTZPresetCmd.GOTO_PRESET, 1))
		{
			System.out.println("PTZPreset  GOTO_PRESET faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZPreset  GOTO_PRESET succ");
		}
		
	}
	public static void Test_PTZCruise(int iPreviewID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZCruise(iPreviewID, PTZCruiseCmd.RUN_SEQ, (byte)1,(byte)1,(short)1))
		{
			System.out.println("PTZCruise  RUN_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZCruise  RUN_SEQ succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_PTZCruise(iPreviewID, PTZCruiseCmd.STOP_SEQ, (byte)1,(byte)1,(short)1))
		{
			System.out.println("PTZCruise  STOP_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("PTZCruise  STOP_SEQ succ");
		}
	}
	public static void Test_PTZTrack(int iPreviewID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZTrack(iPreviewID, PTZTrackCmd.RUN_CRUISE))
		{
			System.out.println("NET_DVR_PTZTrack  RUN_CRUISE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZTrack  RUN_CRUISE succ");
		}
	}
	public static void Test_PTZSelZoomIn(int iPreviewID)
	{
		NET_DVR_POINT_FRAME strPointFrame = new NET_DVR_POINT_FRAME();
    	strPointFrame.xTop = 10;
    	strPointFrame.yTop = 20;
    	strPointFrame.xBottom = 30;
    	strPointFrame.yBottom = 40;
    	if(!HCNetSDK.getInstance().NET_DVR_PTZSelZoomIn(iPreviewID, strPointFrame))
		{
    		System.out.println("NET_DVR_PTZSelZoomIn!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());	
		}
    	else
    	{
    		System.out.println("NET_DVR_PTZSelZoomIn! succeed");	
    	}
	}
	
	public static void Test_PTZControl_Other(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(iUserID, iChan, PTZCommand.TILT_UP, 0))
		{
			System.out.println("NET_DVR_PTZControl_Other  TILT_UP 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZControl_Other  TILT_UP 0 succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_PTZControl_Other(iUserID, iChan, PTZCommand.TILT_UP, 1))
		{
			System.out.println("NET_DVR_PTZControl_Other  TILT_UP 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZControl_Other  TILT_UP 1 succ");
		}
	}
	public static void Test_PTZControlWithSpeed_Other(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed_Other(iUserID, iChan,PTZCommand.PAN_RIGHT, 0, 4))
		{
			System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 0 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 0 succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_PTZControlWithSpeed_Other(iUserID, iChan,PTZCommand.PAN_RIGHT, 1, 4))
		{
			System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 1 faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZControlWithSpeed_Other  PAN_RIGHT 1 succ");
		}
	}
	public static void Test_PTZPreset_Other(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZPreset_Other(iUserID, iChan, PTZPresetCmd.GOTO_PRESET, 1))
		{
			System.out.println("NET_DVR_PTZPreset_Other  GOTO_PRESET faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZPreset_Other  GOTO_PRESET succ");
		}
	}
	public static void Test_PTZCruise_Other(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZCruise_Other(iUserID, iChan, PTZCruiseCmd.RUN_SEQ, (byte)1,(byte)1,(short)1))
		{
			System.out.println("NET_DVR_PTZCruise_Other  RUN_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZCruise_Other  RUN_SEQ succ");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_PTZCruise_Other(iUserID, iChan, PTZCruiseCmd.STOP_SEQ, (byte)1,(byte)1,(short)1))
		{
			System.out.println("NET_DVR_PTZCruise_Other  STOP_SEQ faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZCruise_Other  STOP_SEQ succ");
		}
	}
	public static void Test_PTZTrack_Other(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_PTZTrack_Other(iUserID, iChan, PTZTrackCmd.RUN_CRUISE))
		{
			System.out.println("NET_DVR_PTZTrack_Other  RUN_CRUISE faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_PTZTrack_Other  RUN_CRUISE succ");
		}
	}
	public static void Test_PTZSelZoomIn_EX(int iUserID, int iChan)
	{
		NET_DVR_POINT_FRAME strPointFrame = new NET_DVR_POINT_FRAME();
    	strPointFrame.xTop = 10;
    	strPointFrame.yTop = 20;
    	strPointFrame.xBottom = 30;
    	strPointFrame.yBottom = 40;
    	if(!HCNetSDK.getInstance().NET_DVR_PTZSelZoomIn_EX(iUserID, iChan, strPointFrame))
		{
    		System.out.println("NET_DVR_PTZSelZoomIn_EX failed" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());	
		}
    	else
    	{
    		System.out.println("NET_DVR_PTZSelZoomIn_EX! succeed");	
		}
	}


	public static void TEST_PTZ(int iPlayID, int iLogID, int iStartChan)
	{
		Test_PTZControl(iPlayID);
		Test_PTZControlWithSpeed(iPlayID);
		Test_PTZPreset(iPlayID);
		Test_PTZCruise(iPlayID);
		Test_PTZTrack(iPlayID);
		Test_PTZSelZoomIn(iPlayID);
		Test_PTZControl_Other(iLogID, iStartChan);
		Test_PTZControlWithSpeed_Other(iLogID, iStartChan);
		Test_PTZPreset_Other(iLogID, iStartChan);
		Test_PTZCruise_Other(iLogID, iStartChan);
		Test_PTZTrack_Other(iLogID, iStartChan);
		Test_PTZSelZoomIn_EX(iLogID, iStartChan);
	}
}
