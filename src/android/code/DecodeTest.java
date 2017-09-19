package com.test.demo;

import android.util.Log;

import com.hikvision.netsdk.COND_INT_PTR;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.INT_PTR;
import com.hikvision.netsdk.NET_DVR_IPC_PROTO_LIST_V41;
import com.hikvision.netsdk.NET_DVR_MATRIX_DEC_CHAN_INFO_V41;
import com.hikvision.netsdk.NET_DVR_PU_STREAM_CFG_V41;
import com.hikvision.netsdk.NET_DVR_SIGNAL_JOINT_CFG;
import com.hikvision.netsdk.NET_DVR_START_PIC_VIEW_INFO;
import com.hikvision.netsdk.PicDataCallback;

public class DecodeTest {
	private final static String TAG	= "PicutreTest";
	private static PicDataCallback pDataCallback = null;
	
	public static void WinDecInfo(int iUserID)
	{
		//子窗口数组最大256
        COND_INT_PTR[] conditionArray = new COND_INT_PTR[256];
        for (int i = 0; i < conditionArray.length; ++i)
        {
            conditionArray[i] = new COND_INT_PTR();
            
        }
        conditionArray[0].iValue = 0x01010001;
        conditionArray[1].iValue = 0x01020001;
        conditionArray[2].iValue = 0x01030001;
        conditionArray[3].iValue = 0x01040001;
        conditionArray[4].iValue = 0x01050001;
        conditionArray[5].iValue = 0x01060001;
        conditionArray[6].iValue = 0x01070001;
        conditionArray[7].iValue = 0x01080001;
        conditionArray[8].iValue = 0x01090001;
        conditionArray[9].iValue = 0x010a0001;
        

        //遍历所有窗口，子窗口号赋值给conditionArray，并计算出实际的子窗口数。
        int subWindowCount = 9;
        /*
        for (VideoWallWindow window : windowList)
        {
            for (int i = 0; i < window.getWindowMode(); ++i)
            {
                //（1字节墙号+1字节子窗口号+2字节窗口号），子窗口号从1开始
                conditionArray[subWindowCount].iValue = (videoWallNo << 24) | ((i + 1) << 16) | window.getWindowNo();

                subWindowCount++;
            }
        }*/


        //状态数组
        int[] statusArray = new int[subWindowCount];

        //创建一个subWindowCount大小的数组（传给SDK接口），从conditionArray拷贝数据
        COND_INT_PTR[] windowNoArray = new COND_INT_PTR[subWindowCount];
        for (int i = 0; i < windowNoArray.length; ++i)
        {
            windowNoArray[i] = conditionArray[i];
        }

        //解码信息数组
        NET_DVR_MATRIX_DEC_CHAN_INFO_V41[] decInfoArray = new NET_DVR_MATRIX_DEC_CHAN_INFO_V41[subWindowCount];
        for (int i = 0; i < decInfoArray.length; ++i)
        {
            decInfoArray[i] = new NET_DVR_MATRIX_DEC_CHAN_INFO_V41();
        }

        INT_PTR outPtr = new INT_PTR();

        if (!HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.NET_DVR_GET_WIN_DEC_INFO, subWindowCount,
                statusArray, windowNoArray, decInfoArray, outPtr))
        {
        	Log.e(TAG,"Get window dec info failed! " + HCNetSDK.getInstance().NET_DVR_GetLastError());
        }else {
        	
            	Log.i(TAG,"Get window dec info success ");
            
		}

		
		int dwCount = 2;
		int lpStatus[] = new int[2];
		COND_INT_PTR []lpInt_PTR = new COND_INT_PTR[dwCount];
		lpInt_PTR[0] = new COND_INT_PTR();
		lpInt_PTR[0].iValue = 0x01010001;
		lpInt_PTR[1] = new COND_INT_PTR();
		lpInt_PTR[1].iValue = 0x01010002;
		NET_DVR_MATRIX_DEC_CHAN_INFO_V41[] struInfo = new NET_DVR_MATRIX_DEC_CHAN_INFO_V41[dwCount];
		struInfo[0] = new NET_DVR_MATRIX_DEC_CHAN_INFO_V41();
		struInfo[1]= new NET_DVR_MATRIX_DEC_CHAN_INFO_V41();
		INT_PTR intPtr = new INT_PTR();
		if(HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_WIN_DEC_INFO,dwCount, lpStatus,lpInt_PTR,struInfo,intPtr))
		{
			Log.i(TAG,"NET_DVR_GET_WIN_DEC_INFO success");
			Log.i(TAG,"struInfo[0].byStreamMode="+struInfo[0].byStreamMode);
			for(int i=0;i<struInfo[0].uDecStreamMode.struDecStreamDev.struDevChanInfo.byAddress.length;i++)
			{
				System.out.println(struInfo[0].uDecStreamMode.struDecStreamDev.struDevChanInfo.byAddress[i]);
				
			}
			String string = new String(struInfo[0].uDecStreamMode.struDecStreamDev.struDevChanInfo.byAddress);
			Log.i(TAG,"struInfo[0].uDecStreamMode.struDecStreamDev.struDevChanInfo.byAddress="+string);
		}
		else {
			Log.e(TAG, "NET_DVR_GET_WIN_DEC_INFO fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	
	public static void StartDyanmic(int iUserID)
	{
		int dwDecChanNum = 0x01010001;
		NET_DVR_PU_STREAM_CFG_V41 struStreamCfg = new NET_DVR_PU_STREAM_CFG_V41();
		struStreamCfg.byStreamMode = 1;
		
		//struStreamCfg.uDecStreamMode.struDecStreamDev.struStreamMediaSvrCfg = ;
		byte [] byAddress = "10.17.132.160".getBytes();	
		for(int i=0;i<byAddress.length;i++)
		{
			struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.byAddress[i] = byAddress[i];
		}
		struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.wDVRPort = 8000;
		byte [] byUserName = "admin".getBytes();
		for(int i=0;i<byUserName.length;i++)
		{
			struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.sUserName[i]=byUserName[i];
		}
		
		byte [] byPwd = "hik12345".getBytes();
		for(int i=0;i<byPwd.length;i++)
		{
			struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.sPassword[i]=byPwd[i];
		}
		struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.byChannel = 1;
		struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.dwChannel = 0;
		//struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.byChanType=3;
		//struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.byTransProtocol = 0;
		//struStreamCfg.stuDecStreamMode.struDecStreamDev.struDevChanInfo.byTransMode = 0;
		if(HCNetSDK.getInstance().NET_DVR_MatrixStartDynamic_V41(iUserID, dwDecChanNum, struStreamCfg))
		{
			Log.i(TAG,"NET_DVR_MatrixStartDynamic_V41 success");
		}
		else {
			Log.e(TAG, "NET_DVR_MatrixStartDynamic_V41 fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		if(HCNetSDK.getInstance().NET_DVR_MatrixStopDynamic(iUserID, dwDecChanNum))
			{
				Log.i(TAG,"NET_DVR_MatrixStopDynamic success!");
			}
			else {
				Log.e(TAG,"NET_DVR_MatrixStopDynamic  fail,error code ="+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		struStreamCfg.byStreamMode = 2;
		String strUrl ="www.baidu.com";
		for(int i=0;i<strUrl.length();i++)
		struStreamCfg.stuDecStreamMode.struUrlInfo.strURL[i] = (byte)strUrl.charAt(i);
		if(HCNetSDK.getInstance().NET_DVR_MatrixStartDynamic_V41(iUserID, dwDecChanNum, struStreamCfg))
		{
			Log.i(TAG,"NET_DVR_MatrixStartDynamic_V41 success");
		}
		else {
			Log.e(TAG, "NET_DVR_MatrixStartDynamic_V41 fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		struStreamCfg.byStreamMode = 3;
		if(HCNetSDK.getInstance().NET_DVR_MatrixStartDynamic_V41(iUserID, dwDecChanNum, struStreamCfg))
		{
			Log.i(TAG,"NET_DVR_MatrixStartDynamic_V41 success");
		}
		else {
			Log.e(TAG, "NET_DVR_MatrixStartDynamic_V41 fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
	}
	
	
	public static void GetIPC(int iUserID)
	{
		NET_DVR_IPC_PROTO_LIST_V41 struList = new NET_DVR_IPC_PROTO_LIST_V41();
		Log.i(TAG,"java dwNum = "+struList.dwProtoNum);
		if(HCNetSDK.getInstance().NET_DVR_GetIPCProtoList_V41(iUserID, struList))
		{
			Log.i(TAG,"NET_DVR_GetIPCProtoList_V41 success");
		}
		else {
			Log.e(TAG, "NET_DVR_GetIPCProtoList_V41 fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		Log.i(TAG,"java dwNum = "+struList.dwProtoNum);
	}
	
	private static void processPicData(int nScreenPicHandle,int dwDataType,byte [] pBuffer,int dwBufSize)
    {
		System.out.println("nScreenPicHandle " + nScreenPicHandle +" dwDataType "+dwDataType+" dwBufSize " + dwBufSize);
    }
	
	public static void PicPreview(int iUserID)
	{
		
		if(pDataCallback == null)
		{
			pDataCallback = new PicDataCallback()
			{
				public void fPicDataCallback(int nScreenPicHandle,int dwDataType,byte [] pBuffer,int dwBufSize)
				{
					processPicData(nScreenPicHandle,dwDataType, pBuffer, dwBufSize);
			    }
			};
		}
		
		NET_DVR_START_PIC_VIEW_INFO struPicPre = new NET_DVR_START_PIC_VIEW_INFO();
		struPicPre.dwSignalIndex = 33;
		struPicPre.dwDeviceIndex = 0;
		struPicPre.dwScreenNum = 0;
		struPicPre.byChanIndex = 0;
		struPicPre.dwLayer = 0;
		struPicPre.dwResolution = 0;
		struPicPre.byFrame = 0;
		//struPicPre.bySupportStreamView=1;
		
		int nScreenPicHandle = HCNetSDK.getInstance().NET_DVR_StartPicPreview(iUserID,struPicPre,pDataCallback);
		if(nScreenPicHandle >= 0)
		{
			Log.i(TAG,"NET_DVR_StartPicPreview success");
		}
		else {
			Log.e(TAG, "NET_DVR_StartPicPreview fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		/*if(HCNetSDK.getInstance().NET_DVR_StopScreenPic(nScreenPicHandle))
		{
			Log.i(TAG,"NET_DVR_StopScreenPic success");
		}
		else {
			Log.e(TAG, "NET_DVR_StopScreenPic fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}*/
		
	}
	
	public static void JointCfg(int iUserID)
	{
		
		NET_DVR_SIGNAL_JOINT_CFG struCfg= new NET_DVR_SIGNAL_JOINT_CFG();
		if(HCNetSDK.getInstance().NET_DVR_GetDVRConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_SIGNAL_JOINT, 1, struCfg))
		{
			Log.i(TAG,"NET_DVR_GET_SIGNAL_JOINT success");
		}
		else 
		{
			Log.e(TAG, "NET_DVR_GET_SIGNAL_JOINT fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		NET_DVR_SIGNAL_JOINT_CFG []arrCfg= new NET_DVR_SIGNAL_JOINT_CFG[64];
		for(int i=0;i<64;i++)
		{
			arrCfg[i]= new NET_DVR_SIGNAL_JOINT_CFG();
		}
		int []arrStatus=new int[64];
		INT_PTR nRet=new INT_PTR();
		nRet.iValue = 0;
		if(!HCNetSDK.getInstance().NET_DVR_GetDeviceConfig(iUserID, HCNetSDK.getInstance().NET_DVR_GET_ALL_SIGNAL_JOINT, 0xffffffff, arrStatus, null, arrCfg,nRet))
		{
			Log.i(TAG,"NET_DVR_GET_ALL_SIGNAL_JOINT success");
		}
		else 
		{
			Log.e(TAG, "NET_DVR_GET_ALL_SIGNAL_JOINT fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		COND_INT_PTR [] arrNo = new COND_INT_PTR[1];
		arrNo[0] = new COND_INT_PTR();
		arrNo[0].iValue=1;
		NET_DVR_SIGNAL_JOINT_CFG []arrCfg1= new NET_DVR_SIGNAL_JOINT_CFG[1];
		arrCfg1[0]= new NET_DVR_SIGNAL_JOINT_CFG();
		arrCfg1[0]=struCfg;
		
		/*arrCfg1[0].byEnable=1;
		arrCfg1[0].byColumns=1;
		arrCfg1[0].byRows=2;
		arrCfg1[0].byCamMode=15;
		arrCfg1[0].dwSignalNo[0]=33;
		arrCfg1[0].dwSignalNo[1]=34;*/
		
		int []arrOut=new int[1];
		int[]arrStatus1=new int[1];
		if(HCNetSDK.getInstance().NET_DVR_SetDeviceConfigEx(iUserID, HCNetSDK.getInstance().NET_DVR_SET_SIGNAL_JOINT, 1, arrNo, arrCfg1, arrStatus1,0,arrOut))
		{
			Log.i(TAG,"NET_DVR_SET_SIGNAL_JOINT success");
		}
		else 
		{
			Log.e(TAG, "NET_DVR_SET_SIGNAL_JOINT fail,error code = "+HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	

}
