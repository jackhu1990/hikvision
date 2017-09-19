package com.test.demo;

import java.io.File;
import java.util.Arrays;

import android.util.Log;

import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_BASEMAP_CONTROL_CFG;
import com.hikvision.netsdk.NET_DVR_BASEMAP_PIC_INFO;
import com.hikvision.netsdk.NET_DVR_BASEMAP_WINCFG;
import com.hikvision.netsdk.NET_DVR_PICTURECFG;

public class PictureTest {
	private final static String 	TAG		= "PicutreTest";
	private static int m_iHandle = -1;
	
	public static void PicUpload(int iUserID) throws InterruptedException
	{
		NET_DVR_PICTURECFG struPic= new NET_DVR_PICTURECFG();
		struPic.byUseType = 1;
		struPic.bySequence = 1;
		struPic.sPicName[0]='a';
		struPic.sPicName[1]='b';
		struPic.dwVideoWallNo = 0x0100000;
		struPic.struBasemapCfg.byScreenIndex = 1;
		struPic.struBasemapCfg.byMapNum = 1;
		struPic.struBasemapCfg.wSourHeight = 1;
		struPic.struBasemapCfg.wSourWidth = 1;
		String sFileName = "/mnt/sdcard/Pictures/Screenshots/Screenshot_2015-10-12-21-27-05.png";
		
		//String sFileName = "/mnt/sdcard/test.bmp";
		
		File f = new File(sFileName);
		if (f.exists()) {
			Log.i(TAG, "exist "+sFileName);

		} else {
			Log.i(TAG, "not exist "+sFileName);
		}
		/*
         String filepath= "/mnt/sdcard/Pictures/Screenshots";
         File file = new File(filepath);
         if(file.isDirectory()) {
             System.out.println("文件夹");
             String[] filelist = file.list();
             for (int i = 0; i < filelist.length; i++) {
                     File readfile = new File(filepath + "\\" + filelist[i]);
                     if (!readfile.isDirectory()) {
                    	 Log.i(TAG, "path=" + readfile.getPath());
                    	 Log.i(TAG, "absolutepath="
                                             + readfile.getAbsolutePath());
                    	 Log.i(TAG, "name=" + readfile.getName());
                    	 Log.i(TAG,"file.length="+readfile.length());
                     } else if (readfile.isDirectory()) {
                            // readfile(filepath + "\\" + filelist[i]);
                    	 Log.i(TAG, "is directy");
                     }
             }

     }*/
		
		m_iHandle = HCNetSDK.getInstance().NET_DVR_PicUpload(iUserID, sFileName, struPic);
		if(m_iHandle == -1)
		{
			Log.e(TAG,"NET_DVR_PicUpload fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}
		else {
			Log.i(TAG,"NET_DVR_PicUpload success");
		}
		
		for(int i=0;i<100;i++)
		{
			int iprogress = HCNetSDK.getInstance().NET_DVR_GetPicUploadProgress(m_iHandle);
			Log.i(TAG,"i="+i+",---iprogress="+iprogress);
			Thread.sleep(10);
		}
		
		if(HCNetSDK.getInstance().NET_DVR_CloseUploadHandle(m_iHandle))
		{
			Log.i(TAG,"NET_DVR_CloseUploadHandle success");
		}
		else {
			Log.e(TAG,"NET_DVR_CloseUploadHandle fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		}
		
	}
	
	public static void BaseMap(int iUserID)
	{
		int lChannel = 0x01000001;
		NET_DVR_BASEMAP_WINCFG struBaseMap = new NET_DVR_BASEMAP_WINCFG();
		if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_WIN_CFG, lChannel, struBaseMap))
		{
			Log.i(TAG,"NET_DVR_GET_BASEMAP_WIN_CFG success");
		}
		else {
			Log.e(TAG,"NET_DVR_GET_BASEMAP_WIN_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		}
		struBaseMap.byEnable = 1;
		struBaseMap.struWinPosition.dwHeight = 1920;
		struBaseMap.struWinPosition.dwWidth = 1920;
		struBaseMap.struWinPosition.dwXCoordinate = 0;
		struBaseMap.struWinPosition.dwYCoordinate = 0;
		
		if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_BASEMAP_WIN_CFG, lChannel, struBaseMap))
		{
			Log.i(TAG,"NET_DVR_SET_BASEMAP_WIN_CFG success");
		}
		else {
			Log.e(TAG,"NET_DVR_SET_BASEMAP_WIN_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		}
				
	}
	
	public static void BasemapCfg(int iUserID)
	{
		int lChannel = 1;
		NET_DVR_BASEMAP_PIC_INFO struPicInfo = new NET_DVR_BASEMAP_PIC_INFO();
		if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID,HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_PIC_INFO,lChannel, struPicInfo))
		{
			Log.i(TAG,"NET_DVR_GET_BASEMAP_PIC_INFO success");
		}
		else {
			Log.e(TAG,"NET_DVR_GET_BASEMAP_PIC_INFO fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		}
		
		NET_DVR_BASEMAP_CONTROL_CFG strCtrlCfg = new NET_DVR_BASEMAP_CONTROL_CFG();
		lChannel = 0x01000001;
		if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_BASEMAP_CFG, lChannel, strCtrlCfg))
		{
			Log.i(TAG,"NET_DVR_GET_BASEMAP_CFG success");
		}
		else {
			Log.e(TAG,"NET_DVR_GET_BASEMAP_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		
		}
		
		if(HCNetSDK.getInstance().NET_DVR_SetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_SET_BASEMAP_CFG, lChannel, strCtrlCfg))
		{
			Log.i(TAG,"NET_DVR_SET_BASEMAP_CFG success");
		}
		else {
			Log.e(TAG,"NET_DVR_SET_BASEMAP_CFG fail,error coed = "+HCNetSDK.getInstance().NET_DVR_GetLastError());			
		
		}
	}
	
	
}
