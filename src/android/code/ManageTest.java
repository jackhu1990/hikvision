package com.test.demo;
import com.hikvision.netsdk.*;
public class ManageTest {
	public static void Test_SearchLog(int iUserID)
	{
	  	NET_DVR_TIME struStartTime = new NET_DVR_TIME();
	  	NET_DVR_TIME struEndTime = new NET_DVR_TIME();
	   	struStartTime.dwYear = 2014;
	   	struStartTime.dwMonth = 11;
	   	struStartTime.dwDay = 26;
	   	struEndTime.dwYear = 2014;
	   	struEndTime.dwMonth = 11;
	   	struEndTime.dwDay = 27;
	   	int nSearchHandle = (int)HCNetSDK.getInstance().NET_DVR_FindDVRLog_V30(iUserID, 0, 0, 0, struStartTime, struEndTime, false);
	   	if(nSearchHandle >= 0)
	   	{
	   		NET_DVR_LOG_V30	struLog = new NET_DVR_LOG_V30();
	   		int nState = -1;
	   		int nCount = 0;
	   		while(true)
	   		{
	   			nState = (int)HCNetSDK.getInstance().NET_DVR_FindNextLog_V30((long)nSearchHandle, struLog);
	   			if(nState != HCNetSDK.NET_DVR_FILE_SUCCESS && nState != HCNetSDK.NET_DVR_ISFINDING)
	   			{
	   				break;
	   			}
	   			else if(nState == HCNetSDK.NET_DVR_FILE_SUCCESS)
	   			{
	   				nCount++;
	   				System.out.println("find log time:" + struLog.strLogTime.dwHour + "-" + struLog.strLogTime.dwMinute + "-" + struLog.strLogTime.dwSecond);
	   			}
	   			try 
	   			{
					Thread.sleep(100);
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
	   		}
	   		HCNetSDK.getInstance().NET_DVR_FindLogClose_V30(nSearchHandle);
	   	}
	}

	public static void Test_ShutDown(int iUserID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_ShutDownDVR(iUserID))
		{
			System.out.println("NET_DVR_ShutDownDVR faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_ShutDownDVR succ!");
		}
	}
	
	public static void Test_RebootDVR(int iUserID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_RebootDVR(iUserID))
		{
			System.out.println("NET_DVR_RebootDVR faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_RebootDVR succ!");
		}
	}
	
	public static void Test_ClickKey(int iUserID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_ClickKey(iUserID, NET_DVR_KEY_CODE.KEY_CODE_MENU))
		{
			System.out.println("NET_DVR_ClickKey faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_ClickKey succ!");
		}
	}
	
	public static void Test_FormatDisk(int iUserID)
	{
		int lFormatHandle = HCNetSDK.getInstance().NET_DVR_FormatDisk(iUserID, 0);
		if(lFormatHandle >= 0)
		{
			INT_PTR ptrCurrentDisk = new INT_PTR();
			INT_PTR ptrCurrentPos = new INT_PTR();
			INT_PTR ptrFormatStatic = new INT_PTR();
			ptrCurrentDisk.iValue = 0;
			ptrCurrentPos.iValue = 0;
			while(ptrFormatStatic.iValue == 0)
			{
				if(!HCNetSDK.getInstance().NET_DVR_GetFormatProgress(lFormatHandle, ptrCurrentDisk, ptrCurrentPos, ptrFormatStatic))
				{
					System.out.println("NET_DVR_GetFormatProgress failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
					break;
				}
				else
				{
					System.out.println("NET_DVR_GetFormatProgress succ Disk:" + ptrCurrentDisk.iValue + " Pos:" + ptrCurrentPos.iValue + " Static:" + ptrFormatStatic.iValue);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Get progress over Disk:" + ptrCurrentDisk.iValue + " Pos:" + ptrCurrentPos.iValue + " Static:" + ptrFormatStatic.iValue);
			
			if(!HCNetSDK.getInstance().NET_DVR_CloseFormatHandle(lFormatHandle))
			{
				System.out.println("NET_DVR_CloseFormatHandle failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
		}
		else
		{
			System.out.println("NET_DVR_FormatDisk failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}

	public static void Test_Upgrade(int iUserID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_SetNetworkEnvironment(0))
		{
			System.out.println("NET_DVR_SetNetworkEnvironment err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		int nUpgradeHandle = HCNetSDK.getInstance().NET_DVR_Upgrade(iUserID, "/mnt/sdcard/digicap.dav");
		if(nUpgradeHandle == -1)
		{
			System.out.println("NET_DVR_Upgrade err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}
		else
		{
			int nProgress = 0;
			int nState = 0;
			INT_PTR ptrSubProgress = new INT_PTR();
			while(nProgress != -1 && nProgress != 100)
			{
				nProgress = HCNetSDK.getInstance().NET_DVR_GetUpgradeProgress(nUpgradeHandle);
				System.out.println("NET_DVR_GetUpgradeProgress with:" + nProgress);
				nState = HCNetSDK.getInstance().NET_DVR_GetUpgradeState(nUpgradeHandle);
				System.out.println("NET_DVR_GetUpgradeState with:" + nState);
				nState = HCNetSDK.getInstance().NET_DVR_GetUpgradeStep(nUpgradeHandle, ptrSubProgress);
				System.out.println("NET_DVR_GetUpgradeStep with SubProgress:" + ptrSubProgress.iValue + " return value:" + nState);
				
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(nProgress == -1)
			{
				System.out.println("NET_DVR_GetUpgradeProgress err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
			if(!HCNetSDK.getInstance().NET_DVR_CloseUpgradeHandle(nUpgradeHandle))
			{
				System.out.println("NET_DVR_CloseUpgradeHandle err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
		}
	}

	public static void Test_ActivateDevice()
	{
	    NET_DVR_ACTIVATECFG	activateCfg = new NET_DVR_ACTIVATECFG();
		System.arraycopy("Abcd1234".getBytes(), 0, activateCfg.sPassword, 0, "Abcd1234".getBytes().length);
		if(!HCNetSDK.getInstance().NET_DVR_ActivateDevice("10.10.35.16", 8000, activateCfg))
		{
			System.out.println("NET_DVR_ActivateDevice failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_ActivateDevice succ");
		}
	}

	
	
	public static void TEST_Manage(int iUserID)
	{
		Test_SearchLog(iUserID);
		Test_ShutDown(iUserID);
		Test_RebootDVR(iUserID);
		Test_ClickKey(iUserID);
		Test_FormatDisk(iUserID);
		Test_Upgrade(iUserID);
		Test_ActivateDevice();
	}
}
