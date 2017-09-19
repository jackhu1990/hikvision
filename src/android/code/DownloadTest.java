package com.test.demo;

import android.util.Log;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
//import com.hikvision.netsdk.NET_DVR_SCREEM_FILE_DOWNLOAD_PARAM;

public class DownloadTest {
	private final String 	TAG						= "DownloadTest";
	private long m_lHandle;
	
	private void startDownload(int iUserID)
	{
		/*NET_DVR_SCREEM_FILE_DOWNLOAD_PARAM downloadParam = new NET_DVR_SCREEM_FILE_DOWNLOAD_PARAM();     
			// HCNetSDK start preview
		int dwDownloadType = 0;
		int dwInBufferSize = 100;
		String sFileName = "sdk/temp";
		m_lHandle = HCNetSDK.getInstance().NET_DVR_StartDownload(iUserID, dwDownloadType, downloadParam, dwInBufferSize, sFileName);
		if (m_lHandle < 0)
		{
			 Log.e(TAG, "NET_DVR_StartDownload is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			Log.i(TAG, "NET_DVR_StartDownload Success!" );
		}*/
	}
	
	private void StopDownload()
	{
		//HCNetSDK.getInstance().NET_DVR_StopDownload(m_lHandle);
		Log.i(TAG, "NET_DVR_StopDownload Success!" );
	}
	
	private void GetDownloadState()
	{
		INT_PTR press = new INT_PTR();;
		//HCNetSDK.getInstance().NET_DVR_GetDownloadState(m_lHandle, press);
	}
	
	

}
