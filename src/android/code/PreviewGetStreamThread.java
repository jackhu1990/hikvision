package com.test.demo;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;

public class PreviewGetStreamThread extends Thread 
{ 
	private int iChan; 
	private int iUserID;
	public PreviewGetStreamThread(int iUserID, int iChan) 
	{ 
		this.iChan = iChan; 
		this.iUserID = iUserID;
	} 
	public void run() 
	{ 
		NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
	    previewInfo.lChannel = iChan;
	    previewInfo.dwStreamType = 1; //substream
	    previewInfo.bBlocked = 1;    
		int iPreviewID = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(iUserID, previewInfo, null);
		if(iPreviewID < 0)
		{
			System.out.println("NET_DVR_RealPlay_V40 failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}			
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HCNetSDK.getInstance().NET_DVR_StopRealPlay(iPreviewID);
		System.out.println("PreviewGetStreamThread " + iChan); 
	}
}
