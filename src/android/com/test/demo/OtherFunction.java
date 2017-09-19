package com.test.demo;

import java.text.SimpleDateFormat;


import android.util.Log;

import com.hikvision.netsdk.*;


public class OtherFunction {
	
	private final static String 	TAG						= "OtherFunction";
	private static SerialDataCallBackV40 SerailDataCbfV40 = null;

	public static void Test_FindFile(int iUserID)
	{
		int iFindHandle = -1;
        NET_DVR_FILECOND lpSearchInfo = new NET_DVR_FILECOND();
        lpSearchInfo.lChannel = 5;
        lpSearchInfo.dwFileType = 0xff;
        lpSearchInfo.dwIsLocked = 0xff;
        lpSearchInfo.dwUseCardNo = 0;
        lpSearchInfo.struStartTime.dwYear = 2014;
        lpSearchInfo.struStartTime.dwMonth = 11;
        lpSearchInfo.struStartTime.dwDay = 26;
        lpSearchInfo.struStopTime.dwYear = 2014;
        lpSearchInfo.struStopTime.dwMonth = 11;
        lpSearchInfo.struStopTime.dwDay = 27;
        iFindHandle = HCNetSDK.getInstance().NET_DVR_FindFile_V30(iUserID, lpSearchInfo);
        if (iFindHandle == -1)
         {
        	System.out.println("NET_DVR_FindFile_V30 failed,Error:" + HCNetSDK.getInstance().NET_DVR_GetLastError()); 	
        	return;
         }
         int findNext = 0;
         NET_DVR_FINDDATA_V30 struFindData = new NET_DVR_FINDDATA_V30();
         while (findNext != -1)
         {
             findNext = HCNetSDK.getInstance().NET_DVR_FindNextFile_V30(iFindHandle, struFindData);
             if (findNext == HCNetSDK.NET_DVR_FILE_SUCCESS)
             {
            	 System.out.println("~~~~~Find File" + CommonMethod.toValidString(new String(struFindData.sFileName)));
            	 System.out.println("~~~~~File Size" + struFindData.dwFileSize);             	 
            	 System.out.println("~~~~~File Time,from" + struFindData.struStartTime.ToString());  
            	 System.out.println("~~~~~File Time,to" + struFindData.struStopTime.ToString());              	 
            	 continue;
             }
             else if (HCNetSDK.NET_DVR_FILE_NOFIND == findNext)
             {
            	 System.out.println("No file found");
                 break;
             }
             else if (HCNetSDK.NET_DVR_NOMOREFILE == findNext)
             {
            	 System.out.println("All files are listed");
                 break;
             }
             else if (HCNetSDK.NET_DVR_FILE_EXCEPTION == findNext)
             {
            	 System.out.println("Exception in searching");
                 break;
             }
             else if (HCNetSDK.NET_DVR_ISFINDING == findNext)
             {
            	 System.out.println("NET_DVR_ISFINDING");
             }
         }
         HCNetSDK.getInstance().NET_DVR_FindClose_V30(iFindHandle);
	}

	public static void Test_FindFileByEvent(int iUserID)
	{
		NET_DVR_SEARCH_EVENT_PARAM struParam = new NET_DVR_SEARCH_EVENT_PARAM(); 
		struParam.wMajorType = MAIN_EVENT_TYPE.EVENT_MOT_DET;
		struParam.wMinorType = (short)0xffff;
		struParam.struStartTime.dwYear = 2014;
		struParam.struStartTime.dwMonth = 05;
		struParam.struStartTime.dwDay = 07;
		struParam.struEndTime.dwYear = 2014;
		struParam.struEndTime.dwMonth = 05;
		struParam.struEndTime.dwDay = 8;
		struParam.wMotDetChanNo[0] = 33;
		struParam.wMotDetChanNo[1] = (short)0xffff;
		
		int iFindHandle = HCNetSDK.getInstance().NET_DVR_FindFileByEvent(iUserID, struParam);
		NET_DVR_SEARCH_EVENT_RET struRet = new NET_DVR_SEARCH_EVENT_RET();
		int iRet = -1;
		if(iFindHandle >= 0)
		{
			while(true)
			{
				iRet = HCNetSDK.getInstance().NET_DVR_FindNextEvent(iFindHandle, struRet);
				if(iRet != HCNetSDK.NET_DVR_FILE_SUCCESS && iRet != HCNetSDK.NET_DVR_ISFINDING)
				{
					System.out.println("find next event exit with: " + iRet);
					break;
				}
				else if(iRet == HCNetSDK.NET_DVR_FILE_SUCCESS)
				{
					System.out.println("event type:" + struRet.wMajorType + ",starttime:" + struRet.struStartTime.dwYear + 
							"-" + struRet.struStartTime.dwMonth + "-" + struRet.struStartTime.dwDay + " " + struRet.struStartTime.dwHour + 
							":" + struRet.struStartTime.dwMinute + ":" + struRet.struStartTime.dwSecond + ",endtime:" + 
							struRet.struEndTime.dwYear + "-" + struRet.struEndTime.dwMonth + "-" + struRet.struEndTime.dwDay + 
							" " + struRet.struEndTime.dwHour + ":" + struRet.struEndTime.dwMinute + ":" + struRet.struEndTime.dwSecond);
				}        						
			}
			HCNetSDK.getInstance().NET_DVR_FindClose_V30(iFindHandle);
		}
		else
		{
			System.out.println("NET_DVR_FindFileByEvent failed: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	
	public static void Test_PlayBackConvert(int iUserID)
	{
	    NET_DVR_TIME timeStart = new NET_DVR_TIME();
	    NET_DVR_TIME timeStop = new NET_DVR_TIME();

	    timeStart.dwYear = 2015;
	    timeStart.dwMonth = 6;
	    timeStart.dwDay = 30;
        timeStop.dwYear = 2015;
        timeStop.dwMonth = 7;
        timeStop.dwDay = 1;

        int nHandle = HCNetSDK.getInstance().NET_DVR_PlayBackByTime(iUserID, 1, timeStart, timeStop);
        if (-1 == nHandle)
        {
		    System.out.println("NET_DVR_PlayBackByTime failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		    return;
	    }
        NET_DVR_COMPRESSION_INFO_V30 compression = new NET_DVR_COMPRESSION_INFO_V30();
        compression.byResolution = 1;
        compression.dwVideoBitrate = 7;
//        HCNetSDK.getInstance().NET_DVR_PlayBackControl_V50(nHandle, PlaybackControlCommand.NET_DVR_PLAY_CONVERT, compression, null);
	    HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
	    int nProgress = -1;
	    while(true)
	    {
	       	nProgress = HCNetSDK.getInstance().NET_DVR_GetPlayBackPos(nHandle);
	       	System.out.println("NET_DVR_GetPlayBackPos:" + nProgress);
	       	if(nProgress < 0 || nProgress >= 100)
	       	{
	       		break;
	       	}
	       	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    HCNetSDK.getInstance().NET_DVR_StopPlayBack(nHandle);	
	}
	
	public static void Test_GetFileByTime(int iUserID)
	{
	    NET_DVR_TIME timeStart = new NET_DVR_TIME();
	    NET_DVR_TIME timeStop = new NET_DVR_TIME();

	    timeStart.dwYear = 2015;
	    timeStart.dwMonth = 05;
	    timeStart.dwDay = 8;
	    timeStart.dwHour = 9;
	    timeStart.dwMinute = 27;
        timeStop.dwYear = 2015;
        timeStop.dwMonth = 05;
        timeStop.dwDay = 8;
        timeStop.dwHour = 10;
        timeStop.dwMinute = 17;

        int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByTime(iUserID,5, timeStart, timeStop, new String("/sdcard/RecordFile"));
        if (-1 == nDownloadHandle)
        {
		    System.out.println("NET_DVR_GetFileByTime failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		    return;
	    }
	    HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
	    int nProgress = -1;
	    while(true)
	    {
	       	nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
	       	System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
	       	if(nProgress < 0 || nProgress >= 100)
	       	{
	       		break;
	       	}
	       	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);	
	}

	public static void Test_GetFileByName(int iUserID)
	{
	    int nDownloadHandle = HCNetSDK.getInstance().NET_DVR_GetFileByName(iUserID, new String("ch0001_01000000080001900"), new String("/sdcard/RecordFile"));
	    if (-1 == nDownloadHandle)
	    {
	    	System.out.println("NET_DVR_GetFileByName failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}
	    HCNetSDK.getInstance().NET_DVR_PlayBackControl_V40(nDownloadHandle, PlaybackControlCommand.NET_DVR_PLAYSTART, null, 0, null);
	    int nProgress = -1;
	    while(true)
	    {
	     	nProgress = HCNetSDK.getInstance().NET_DVR_GetDownloadPos(nDownloadHandle);
	       	System.out.println("NET_DVR_GetDownloadPos:" + nProgress);
	       	if(nProgress < 0 || nProgress >= 100)
	       	{
	       		break;
	       	}
	       	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    HCNetSDK.getInstance().NET_DVR_StopGetFile(nDownloadHandle);	
	}
	
	

	public static void Test_UpdateRecordIndex(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_UpdateRecordIndex(iUserID, iChan))
		{
			System.out.println("NET_DVR_UpdateRecordIndex failed with:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_UpdateRecordIndex succ!");
		}
	}
	
	public static void Test_CaptureJpegPicture(int iUserID, int iChan)
	{
    	NET_DVR_JPEGPARA strJpeg = new  NET_DVR_JPEGPARA();
    	strJpeg.wPicQuality = 1;
    	strJpeg.wPicSize = 2;
    	if(!HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture(iUserID, iChan, strJpeg, new String("/sdcard/cap.jpg")))
    	{
    		System.out.println("NET_DVR_CaptureJPEGPicture!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());	
		}
    	else
    	{
    		System.out.println("NET_DVR_CaptureJPEGPicture! succeed");	
    	}
	}
	
	public static void Test_CaptureJpegPicture_new(int iUserID, int iChan)
	{
		NET_DVR_JPEGPARA strJpeg = new  NET_DVR_JPEGPARA();
    	strJpeg.wPicQuality = 1;
    	strJpeg.wPicSize = 2;
    	int iBufferSize = 1024*1024;
    	byte[] sbuffer = new byte[iBufferSize];
    	INT_PTR bytesRerned = new INT_PTR();
    	if(!HCNetSDK.getInstance().NET_DVR_CaptureJPEGPicture_NEW(iUserID, iChan, strJpeg, sbuffer, iBufferSize, bytesRerned))
    	{
    		System.out.println("NET_DVR_CaptureJPEGPicture_NEW!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());	
		}
    	else
    	{
    		System.out.println("NET_DVR_CaptureJPEGPicture_NEW size!" + bytesRerned.iValue);
    	}
	}

	public static void Test_DVRRecord(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_StartDVRRecord(iUserID, 1, 0))
		{
			System.out.println("NET_DVR_StartDVRRecord err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{
			System.out.println("NET_DVR_StartDVRRecord succ!");
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!HCNetSDK.getInstance().NET_DVR_StopDVRRecord(iUserID, 1))
		{
			System.out.println("NET_DVR_StopDVRRecord err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_StopDVRRecord succ!");
		}
	}

	private static void processSerialData(int lSerialHandle, byte[] pDataBuffer, int iDataSize)
    {
    	System.out.println("lSerialHandle " + lSerialHandle + " iDataSize " + iDataSize);
    }
	public static void Test_TransChannel(int iUserID)
	{
		if(SerailDataCbfV40 == null)
		{
			SerailDataCbfV40 = new SerialDataCallBackV40()
			{
				public void fSerialDataCallBackV40(int lSerialHandle, int lChannel, byte[] pDataBuffer, int iDataSize)
				{
				   processSerialData(lSerialHandle, pDataBuffer, iDataSize);
			    }
			};
		}
		NET_DVR_SERIALSTART_V40 struSerialStart = new NET_DVR_SERIALSTART_V40();
		struSerialStart.dwSerialPort = 2;
		struSerialStart.wPort = 0;
		int lSerialHandle = HCNetSDK.getInstance().NET_DVR_SerialStart_V40(iUserID, struSerialStart, SerailDataCbfV40);
		if(lSerialHandle < 0)
		{
			System.out.println("NET_DVR_SerialStart failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		byte[] bytes = {0x5A,0x5A,0x5A,0x7E,0x0F,0x04,0x07,0x00,0x00,0x01,0x1B,0x00,0x00};
		int length = bytes.length;
		if(!HCNetSDK.getInstance().NET_DVR_SerialSend(lSerialHandle, 0, bytes, length))
		{
			System.out.println("NET_DVR_SerialSend failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		if(!HCNetSDK.getInstance().NET_DVR_SerialStop(lSerialHandle))
		{
			System.out.println("NET_DVR_SerialStop failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	
	public static void Test_Serial(int iUserID)
	{
		if(!HCNetSDK.getInstance().NET_DVR_SendToSerialPort(iUserID, 1, 1, "12345".getBytes(), 5))
		{
			System.out.println("NET_DVR_SendToSerialPort failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SendToSerialPort succ!");
		}
		
		if(!HCNetSDK.getInstance().NET_DVR_SendTo232Port(iUserID, "12345".getBytes(), 5))
		{
			System.out.println("NET_DVR_SendTo232Port failed! error:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		else
		{
			System.out.println("NET_DVR_SendTo232Port succ!");
		}
	}

	private static RealPlayCallBack cbf = null;
	private static void processRealData(int lRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize)
    {
    	System.out.println("recv real stream ,dataType:"+iDataType+", size:" + iDataSize);
    }
	public static void Test_ZeroChanPreview(int iUserID)
	{
		if (cbf==null){
		    cbf = new RealPlayCallBack(){
			    public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize){
				   processRealData(iRealHandle, iDataType, pDataBuffer, iDataSize);
			    }
		    };
		}
    	NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
	    ClientInfo.lChannel = 1;
	    ClientInfo.lLinkMode = 0;
	    int iZeroPreviewHandle = HCNetSDK.getInstance().NET_DVR_ZeroStartPlay(iUserID, ClientInfo, cbf, true);
	    if(iZeroPreviewHandle < 0)
	    {
	    	System.out.println("NET_DVR_ZeroStartPlay failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
	    }
	    else
	    {
	    	System.out.println("NET_DVR_ZeroStartPlay succ");
	    }
	
	    try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		if(!HCNetSDK.getInstance().NET_DVR_ZeroStopPlay(iZeroPreviewHandle))
		{
	    	System.out.println("NET_DVR_ZeroStopPlay failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
	    }
	    else
	    {
	    	System.out.println("NET_DVR_ZeroStopPlay succ");
	    }	
	}
	
	public static void Test_Hikonline()
	{
		NET_DVR_QUERY_COUNTRYID_COND	struCountryIDCond = new NET_DVR_QUERY_COUNTRYID_COND();
		NET_DVR_QUERY_COUNTRYID_RET		struCountryIDRet = new NET_DVR_QUERY_COUNTRYID_RET();
		struCountryIDCond.wCountryID = 248; //248 is for china,other country's ID please see the interface document 
		System.arraycopy("www.hik-online.com".getBytes(), 0, struCountryIDCond.szSvrAddr, 0, "www.hik-online.com".getBytes().length);
		System.arraycopy("Android NetSDK Demo".getBytes(), 0, struCountryIDCond.szClientVersion, 0, "Android NetSDK Demo".getBytes().length);
		//first you need get the resolve area server address form www.hik-online.com by country ID
		//and then get you dvr/ipc address from the area resolve server
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYSVR_BY_COUNTRYID, struCountryIDCond, struCountryIDRet))
		{
			System.out.println("QUERYSVR_BY_COUNTRYID succ,resolve:" + CommonMethod.toValidString(new String(struCountryIDRet.szResolveSvrAddr)));
		}
		else
		{
			System.out.println("QUERYSVR_BY_COUNTRYID failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		//follow code show how to get dvr/ipc address from the area resolve server by nickname or serial no.
		NET_DVR_QUERY_DDNS_COND	struDDNSCond = new NET_DVR_QUERY_DDNS_COND();
		NET_DVR_QUERY_DDNS_RET	struDDNSQueryRet = new NET_DVR_QUERY_DDNS_RET();
		NET_DVR_CHECK_DDNS_RET	struDDNSCheckRet = new NET_DVR_CHECK_DDNS_RET();
		System.arraycopy("Android NetSDK Demo".getBytes(), 0, struDDNSCond.szClientVersion, 0, "Android NetSDK Demo".getBytes().length);
		System.arraycopy(struCountryIDRet.szResolveSvrAddr, 0, struDDNSCond.szResolveSvrAddr, 0, struCountryIDRet.szResolveSvrAddr.length);
		System.arraycopy("nickname".getBytes(), 0, struDDNSCond.szDevNickName, 0, "nickname".getBytes().length);//your dvr/ipc nickname
		System.arraycopy("serial no.".getBytes(), 0, struDDNSCond.szDevSerial, 0, "serial no.".getBytes().length);//your dvr/ipc serial no.
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSQueryRet))
		{
			System.out.println("QUERYDEV_BY_NICKNAME_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSQueryRet.szDevIP)) + ", SDK port:" + struDDNSQueryRet.wCmdPort + ", http port" + struDDNSQueryRet.wHttpPort);
		}
		else
		{
			System.out.println("QUERYDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_SERIAL_DDNS, struDDNSCond, struDDNSQueryRet))
		{
			System.out.println("QUERYDEV_BY_SERIAL_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSQueryRet.szDevIP)) + ", SDK port:" + struDDNSQueryRet.wCmdPort + ", http port" + struDDNSQueryRet.wHttpPort);
		}
		else
		{
			System.out.println("QUERYDEV_BY_SERIAL_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		//if you get the dvr/ipc address failed from the area reolve server,you can check the reason show as follow
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.CHECKDEV_BY_NICKNAME_DDNS, struDDNSCond, struDDNSCheckRet))
		{
			System.out.println("CHECKDEV_BY_NICKNAME_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSCheckRet.struQueryRet.szDevIP)) + ", SDK port:" + struDDNSCheckRet.struQueryRet.wCmdPort + ", http port" + struDDNSCheckRet.struQueryRet.wHttpPort + ",region:" + struDDNSCheckRet.wRegionID + ",status:" + struDDNSCheckRet.byDevStatus);
		}
		else
		{
			System.out.println("CHECKDEV_BY_NICKNAME_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.CHECKDEV_BY_SERIAL_DDNS, struDDNSCond, struDDNSCheckRet))
		{
			System.out.println("CHECKDEV_BY_SERIAL_DDNS succ,ip:" + CommonMethod.toValidString(new String(struDDNSCheckRet.struQueryRet.szDevIP)) + ", SDK port:" + struDDNSCheckRet.struQueryRet.wCmdPort + ", http port" + struDDNSCheckRet.struQueryRet.wHttpPort + ",region:" + struDDNSCheckRet.wRegionID + ",status:" + struDDNSCheckRet.byDevStatus);
		}
		else
		{
			System.out.println("CHECKDEV_BY_SERIAL_DDNS failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}
	
	public static void Test_IPServer()
	{
		NET_DVR_QUERY_IPSERVER_COND	struIPServerCond = new NET_DVR_QUERY_IPSERVER_COND();
		NET_DVR_QUERY_IPSERVER_RET	struIPServerRet = new NET_DVR_QUERY_IPSERVER_RET();
		struIPServerCond.wResolveSvrPort = 7071;
		System.arraycopy("10.10.34.21".getBytes(), 0, struIPServerCond.szResolveSvrAddr, 0, "10.10.34.21".getBytes().length);//your ipserver ip
		System.arraycopy("nickname".getBytes(), 0, struIPServerCond.szDevNickName, 0, "nickname".getBytes().length);//your dvr/ipc nickname on ipserver
		//search by nickname
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_NICKNAME_IPSERVER, struIPServerCond, struIPServerRet))
		{
			System.out.println("QUERYDEV_BY_NICKNAME_IPSERVER succ,ip:" + CommonMethod.toValidString(new String(struIPServerRet.szDevIP)) + ", SDK port:" + struIPServerRet.wCmdPort);
		}
		else
		{
			System.out.println("QUERYDEV_BY_NICKNAME_IPSERVER failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
		
		System.arraycopy("serial no.".getBytes(), 0, struIPServerCond.szDevSerial, 0, "serial no.".getBytes().length);//your dvr/ipc serial no.
		//search bu serial no.
		if(HCNetSDK.getInstance().NET_DVR_GetAddrInfoByServer(ADDR_QUERY_TYPE.QUERYDEV_BY_SERIAL_IPSERVER, struIPServerCond, struIPServerRet))
		{
			System.out.println("QUERYDEV_BY_SERIAL_IPSERVER succ,ip:" + CommonMethod.toValidString(new String(struIPServerRet.szDevIP)) + ", SDK port:" + struIPServerRet.wCmdPort);
		}
		else
		{
			System.out.println("QUERYDEV_BY_SERIAL_IPSERVER failed:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}
	}

	public static void Test_DVRSetConnectTime()
	{
		if(!HCNetSDK.getInstance().NET_DVR_SetConnectTime(3000))
		{
			System.out.println("NET_DVR_SetConnectTime err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{
			System.out.println("NET_DVR_SetConnectTime succ!");
		}
	}
	

	public static void Test_DVRSetReConnect()
	{
		if(!HCNetSDK.getInstance().NET_DVR_SetReconnect(3000,true))
		{
			System.out.println("NET_DVR_SetReconnect err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{	
			System.out.println("NET_DVR_SetReconnect succ!");
		}
	}
	
	
	public static void Test_SDKLOCAL_CFG(int iUserID)
	{
		NET_DVR_SDKLOCAL_CFG SdkLocalCfg = new NET_DVR_SDKLOCAL_CFG();
		if (!HCNetSDK.getInstance().NET_DVR_GetSDKLocalConfig(SdkLocalCfg))
		{
			System.out.println("NET_DVR_GetSDKLocalConfig faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_GetSDKLocalConfig succ!" + "EnableAbilityParse: " + SdkLocalCfg.byEnableAbilityParse );
		}        						
		if (!HCNetSDK.getInstance().NET_DVR_SetSDKLocalConfig(SdkLocalCfg))
		{
			System.out.println("NET_DVR_SetSDKLocalConfig faild!" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}else
		{
			System.out.println("NET_DVR_SetSDKLocalConfig succ!" + "EnableAbilityParse: " + SdkLocalCfg.byEnableAbilityParse );
		}
	}
	
	public static void Test_GetSDKVersion()
	{
		long SDKVersion = -1;
		long SDKBuildVersion = -1;
		SDKVersion = HCNetSDK.getInstance().NET_DVR_GetSDKVersion();
		if( SDKVersion < 0)
		{
			System.out.println("NET_DVR_GetSDKVersion err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{	
			System.out.println("NET_DVR_GetSDKVersion succ!" + SDKVersion);
		}
		SDKBuildVersion = HCNetSDK.getInstance().NET_DVR_GetSDKBuildVersion();
		if( SDKBuildVersion < 0)
		{
			System.out.println("NET_DVR_GetSDKVersion_GetSDKBuildVersion err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{	
			System.out.println("NET_DVR_GetSDKVersion_GetSDKBuildVersion succ!" + SDKBuildVersion );
		}
	}
	
	

	public static void Test_DVRMakeKeyFrame(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_MakeKeyFrame( iUserID, iChan))
		{
			System.out.println("NET_DVR_MakeKeyFrame err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{
			System.out.println("NET_DVR_MakeKeyFrame succ!");
		}
	}
	
	public static void Test_DVRMakeKeyFrameSub(int iUserID, int iChan)
	{
		if(!HCNetSDK.getInstance().NET_DVR_MakeKeyFrameSub( iUserID, iChan))
		{
			System.out.println("NET_DVR_MakeKeyFrameSub err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{
			System.out.println("NET_DVR_MakeKeyFrameSub succ!");
		}
	}
	
	

	public static void Test_SetRecvTimeOut()
	{
		if(!HCNetSDK.getInstance().NET_DVR_SetRecvTimeOut( 5000 ))
		{
			System.out.println("NET_DVR_SetRecvTimeOut err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		}   
		else
		{
			System.out.println("NET_DVR_SetRecvTimeOut succ!");
		}
	}
	
	public static void Test_RecycleGetStream(int iUserID, int iChan)
	{
		int i = 0;
		while(true)
		{
			for(i = 0; i < 16; i++)
			{
				Thread thread = new PreviewGetStreamThread(iUserID, (iChan + i)); 
				thread.start(); 
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}				
	}
	
//	public static void Test_GetCurrentAudioCompress_V50(int iUserID)
//	{
//		NET_DVR_AUDIO_CHANNEL	audioChannel = new NET_DVR_AUDIO_CHANNEL();
//		audioChannel.dwChannelNum = 3;
//		NET_DVR_COMPRESSION_AUDIO	audioCompression = new NET_DVR_COMPRESSION_AUDIO();
//		if(HCNetSDK.getInstance().NET_DVR_GetCurrentAudioCompress_V50(iUserID, audioChannel, audioCompression))
//		{
//			System.out.println("NET_DVR_GetCurrentAudioCompress_V50 succ, type: " + audioCompression.byAudioEncType);
//		}
//		else
//		{
//			System.out.println("NET_DVR_GetCurrentAudioCompress_V50 failed: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
//		}
//	}
//	
	public static void Test_MultiThreadLogin()
	{
		Thread thread = new Thread()
		{
			public void run()
	   		{
			//	while(true)
				{
					int i = 0;
					for(i = 0; i < 100; i++)
					{
						Thread loginThread = new LoginMultiThread();
						loginThread.start();
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
	   		}
	   	};
	   	thread.start();
				
	}
	
	public static void Test_EzvizConfig(int iUserID)
	{		
		NET_DVR_MULTI_ALARMIN_COND struCond = new NET_DVR_MULTI_ALARMIN_COND();
		NET_DVR_ALARMIN_PARAM_LIST struList = new NET_DVR_ALARMIN_PARAM_LIST();
		
		int i = 0;
		
		for (i = 0; i < 64; i++)
		{
			struCond.iZoneNo[i] = -1;
		}
		
		for(i = 0; i < 8; i++)
		{
			struCond.iZoneNo[i] = i;
		}
		
		for(i = 0; i < 400; i++)
		{
			if (!HCNetSDK.getInstance().NET_DVR_GetSTDConfig(iUserID, HCNetSDK.NET_DVR_GET_ALARMIN_PARAM_LIST, struCond, null, struList))
			{
				System.out.println("NET_DVR_GetSTDConfig" + " err: " + HCNetSDK.getInstance().NET_DVR_GetLastError());
			}
			else
			{
				System.out.println("NET_DVR_GetSTDConfig success");
			}
		}

	}
	
	public static void TEST_OtherFunc(int iPreviewID, int iUserID, int iChan)
	{
		Test_EzvizConfig(iUserID);
//		Test_FindFile(iUserID);
		Test_FindFileByEvent(iUserID);
//		Test_GetFileByTime(iUserID);
//		Test_GetFileByName(iUserID);
//		Test_UpdateRecordIndex(iUserID, iChan);
//		Test_CaptureJpegPicture(iUserID, iChan);
//		Test_CaptureJpegPicture_new(iUserID, iChan);
//		Test_DVRRecord(iUserID, iChan);
//		Test_TransChannel(iUserID);
//		Test_Serial(iUserID);
//		Test_ZeroChanPreview(iUserID);
//		Test_Hikonline();
//		Test_IPServer();
//		Test_DVRSetConnectTime();
//		Test_DVRSetReConnect();
//		Test_DVRMakeKeyFrame(iUserID, iChan);
//		Test_DVRMakeKeyFrameSub(iUserID, iChan);
//		Test_GetSDKVersion();
//		Test_SDKLOCAL_CFG(iUserID);
//		Test_SetRecvTimeOut();
//		Test_PlayBackConvert(iUserID);
//		Test_GetCurrentAudioCompress_V50(iUserID);
	}
}
