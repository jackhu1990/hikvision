package com.test.demo;
import android.R.integer;

import com.hikvision.netsdk.*;
public class AlarmTest {
	private static AlarmCallBack_V30 AlarmCbf = null;
    private static void processAlarmData(int lCommand, NET_DVR_ALARMER Alarmer, NET_DVR_BASE_ALARM AlarmInfo)
    {
    	String sIP = new String(Alarmer.sDeviceIP);
    	System.out.println("recv alarm from:" + sIP);	
    	if(lCommand == HCNetSDK.COMM_ITS_PLATE_RESULT)
    	{
    		NET_ITS_PLATE_RESULT strAlarmInfo = (NET_ITS_PLATE_RESULT)AlarmInfo;
        	System.out.println("recv Its Plate Result:" + strAlarmInfo.dwCustomIllegalType);	
    	}
    }
	public static void Test_SetupAlarm(int iUserID)
	{
 		if(AlarmCbf == null)
		{
			AlarmCbf = new AlarmCallBack_V30()
			{
				public void fMSGCallBack( int lCommand, NET_DVR_ALARMER Alarmer, NET_DVR_BASE_ALARM AlarmInfo)
				{
					   processAlarmData(lCommand, Alarmer, AlarmInfo);	  	
				}
			};
		} 
 		int i = 0;
 		if(!HCNetSDK.getInstance().NET_DVR_SetDVRMessageCallBack_V30(AlarmCbf))
 		{
 			i = 1;
 		}
		
 		//布防
 		NET_DVR_SETUPALARM_PARAM struSetupAlarmParam = new NET_DVR_SETUPALARM_PARAM();
 		struSetupAlarmParam.byAlarmInfoType = 1;
 		struSetupAlarmParam.byLevel  = 1;
		int iAlarmHandle = HCNetSDK.getInstance().NET_DVR_SetupAlarmChan_V41(iUserID, struSetupAlarmParam);
 		if (-1 == iAlarmHandle)
		{
			System.out.println("NET_DVR_SetupAlarmChan_V30 failed!" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
 		else
 		{
 			System.out.println("NET_DVR_SetupAlarmChan_V30 succeed!");              		
 		}
		try {
			Thread.sleep(10*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_CloseAlarmChan_V30(iAlarmHandle))
		{
			System.out.println("NET_DVR_CloseAlarmChan_V30 failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
 		else
 		{
 			System.out.println("NET_DVR_CloseAlarmChan_V30 succeed!");              		
 		}
	

	}
}
