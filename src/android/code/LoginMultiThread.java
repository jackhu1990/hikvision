package com.test.demo;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;

public class LoginMultiThread extends Thread{
	public LoginMultiThread()
	{
		
	}
	
	public void run()
	{
		NET_DVR_DEVICEINFO_V30 devInfo = new NET_DVR_DEVICEINFO_V30();
		int lLoginID = HCNetSDK.getInstance().NET_DVR_Login_V30("10.17.132.118", 8000, "admin", "hik12345", devInfo);
		if(lLoginID < 0)
		{
			System.out.println("NET_DVR_Login_V30 failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_Login_V30 succ:" + lLoginID);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HCNetSDK.getInstance().NET_DVR_Logout_V30(lLoginID);
	}
}
