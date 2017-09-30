//
//  OtherTest.m
//  SimpleDemo
//
//  Created by Netsdk on 15/4/17.
//
//

#import <Foundation/Foundation.h>
#import "OtherTest.h"

void g_GetFileName(char* pFileName, char* pExtend)
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString* date;
    NSDateFormatter* formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"YYYY-MM-dd_hh_mm_ss"];
    date = [formatter stringFromDate:[NSDate date]];
    
    sprintf(pFileName, "%s/%s.%s", (char*)documentsDirectory.UTF8String, (char*)date.UTF8String, pExtend);
}

void Test_HikOnline()
{
    NET_DVR_QUERY_COUNTRYID_COND	struCountryIDCond = {0};
    NET_DVR_QUERY_COUNTRYID_RET		struCountryIDRet = {0};
    struCountryIDCond.wCountryID = 248; //248 is for china,other country's ID please see the interface document
    memcpy(struCountryIDCond.szSvrAddr, "www.hik-online.com", strlen("www.hik-online.com"));
    memcpy(struCountryIDCond.szClientVersion, "iOS NetSDK Demo", strlen("iOS NetSDK Demo"));
    //first you need get the resolve area server address form www.hik-online.com by country ID
	//and then get you dvr/ipc address from the area resolve server
    if(NET_DVR_GetAddrInfoByServer(QUERYSVR_BY_COUNTRYID, &struCountryIDCond, sizeof(struCountryIDCond), &struCountryIDRet, sizeof(struCountryIDRet)))
    {
    	NSLog(@"QUERYSVR_BY_COUNTRYID succ,resolve:%s", struCountryIDRet.szResolveSvrAddr);
    }
    else
    {
    	NSLog(@"QUERYSVR_BY_COUNTRYID failed:%d", NET_DVR_GetLastError());
    }
    //follow code show how to get dvr/ipc address from the area resolve server by nickname or serial no.
    NET_DVR_QUERY_DDNS_COND	struDDNSCond = {0};
    NET_DVR_QUERY_DDNS_RET	struDDNSQueryRet = {0};
    NET_DVR_CHECK_DDNS_RET	struDDNSCheckRet = {0};
    memcpy(struDDNSCond.szClientVersion, "iOS NetSDK Demo", strlen("iOS NetSDK Demo"));
    memcpy(struDDNSCond.szResolveSvrAddr, struCountryIDRet.szResolveSvrAddr, strlen(struCountryIDRet.szResolveSvrAddr));
    memcpy(struDDNSCond.szDevNickName, "nickname", strlen("nickname"));//your dvr/ipc nickname
    memcpy(struDDNSCond.szDevSerial, "serial no.", strlen("serial no."));//your dvr/ipc serial no.
    if(NET_DVR_GetAddrInfoByServer(QUERYDEV_BY_NICKNAME_DDNS, &struDDNSCond, sizeof(struDDNSCond), &struDDNSQueryRet, sizeof(struDDNSQueryRet)))
    {
    	NSLog(@"QUERYDEV_BY_NICKNAME_DDNS succ,ip[%s],sdk port[%d]:", struDDNSQueryRet.szDevIP, struDDNSQueryRet.wCmdPort);
    }
    else
    {
    	NSLog(@"QUERYDEV_BY_NICKNAME_DDNS failed:%d", NET_DVR_GetLastError());
    }
    if(NET_DVR_GetAddrInfoByServer(QUERYDEV_BY_SERIAL_DDNS,  &struDDNSCond, sizeof(struDDNSCond), &struDDNSQueryRet, sizeof(struDDNSQueryRet)))
    {
        NSLog(@"QUERYDEV_BY_SERIAL_DDNS succ,ip[%s],sdk port[%d]:", struDDNSQueryRet.szDevIP, struDDNSQueryRet.wCmdPort);
    }
    else
    {
        NSLog(@"QUERYDEV_BY_SERIAL_DDNS failed:%d", NET_DVR_GetLastError());
    }
    
    //if you get the dvr/ipc address failed from the area reolve server,you can check the reason show as follow
    if(NET_DVR_GetAddrInfoByServer(CHECKDEV_BY_NICKNAME_DDNS, &struDDNSCond, sizeof(struDDNSCond), &struDDNSCheckRet, sizeof(struDDNSCheckRet)))
    {
    	NSLog(@"CHECKDEV_BY_NICKNAME_DDNS succ,ip[%s], sdk port[%d], region[%d], status[%d]",struDDNSCheckRet.struQueryRet.szDevIP, struDDNSCheckRet.struQueryRet.wCmdPort, struDDNSCheckRet.wRegionID, struDDNSCheckRet.byDevStatus);
    }
    else
    {
        NSLog(@"CHECKDEV_BY_NICKNAME_DDNS failed[%d]", NET_DVR_GetLastError());
    }
    if(NET_DVR_GetAddrInfoByServer(CHECKDEV_BY_SERIAL_DDNS, &struDDNSCond, sizeof(struDDNSCond), &struDDNSCheckRet, sizeof(struDDNSCheckRet)))
    {
        NSLog(@"CHECKDEV_BY_SERIAL_DDNS succ,ip[%s], sdk port[%d], region[%d], status[%d]",struDDNSCheckRet.struQueryRet.szDevIP, struDDNSCheckRet.struQueryRet.wCmdPort, struDDNSCheckRet.wRegionID, struDDNSCheckRet.byDevStatus);
    }
    else
    {
        NSLog(@"CHECKDEV_BY_SERIAL_DDNS failed[%d]", NET_DVR_GetLastError());
    }
}

void Test_IPServer()
{
    NET_DVR_QUERY_IPSERVER_COND	struIPServerCond = {0};
    NET_DVR_QUERY_IPSERVER_RET	struIPServerRet = {0};
    struIPServerCond.wResolveSvrPort = 7071;
    memcpy(struIPServerCond.szResolveSvrAddr, "10.17.133.23", strlen("10.17.133.23"));//your ipserver ip
    memcpy(struIPServerCond.szDevNickName, "IP CAMERA", strlen("IP CAMERA"));//your dvr/ipc nickname on ipserver
    //search by nickname
    if(NET_DVR_GetAddrInfoByServer(QUERYDEV_BY_NICKNAME_IPSERVER, &struIPServerCond, sizeof(struIPServerCond), &struIPServerRet, sizeof(struIPServerRet)))
    {
    	NSLog(@"QUERYDEV_BY_NICKNAME_IPSERVER succ,ip[%s],sdk port[%d]", struIPServerRet.szDevIP, struIPServerRet.wCmdPort);
    }
    else
    {
    	NSLog(@"QUERYDEV_BY_NICKNAME_IPSERVER failed[%d]", NET_DVR_GetLastError());
    }
    
    memcpy(struIPServerCond.szDevSerial, "DS-2CD4026FWD-A20140811CCCH475523795", strlen("DS-2CD4026FWD-A20140811CCCH475523795"));//your dvr/ipc serial no.
    //search by serial no.
    if(NET_DVR_GetAddrInfoByServer(QUERYDEV_BY_SERIAL_IPSERVER, &struIPServerCond, sizeof(struIPServerCond), &struIPServerRet, sizeof(struIPServerRet)))
    {
        NSLog(@"QUERYDEV_BY_SERIAL_IPSERVER succ,ip[%s],sdk port[%d]", struIPServerRet.szDevIP, struIPServerRet.wCmdPort);
    }
    else
    {
        NSLog(@"QUERYDEV_BY_SERIAL_IPSERVER failed[%d]", NET_DVR_GetLastError());
    }
}

void Test_Activate()
{
    NET_DVR_ACTIVATECFG struActivate = {0};
    struActivate.dwSize = sizeof(struActivate);
    memcpy(struActivate.sPassword, "Test12345", strlen("Test12345"));
    if(!NET_DVR_ActivateDevice("10.17.132.19", 8000, &struActivate))
    {
        NSLog(@"NET_DVR_ActivateDevice failed[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_ActivateDevice succ");
    }
}

void Test_CaptureJPEGPicture(int iUserID, int iChan)
{
    NET_DVR_JPEGPARA struJpegPara = {0};
    struJpegPara.wPicSize = 2;
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    char szFileName[256] = {0};
    NSString* date;
    NSDateFormatter* formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];
    date = [formatter stringFromDate:[NSDate date]];
    sprintf(szFileName, "%s/%s.jpg", (char*)documentsDirectory.UTF8String, (char*)date.UTF8String);
    if(!NET_DVR_CaptureJPEGPicture(iUserID, iChan, &struJpegPara, szFileName))
    {
        NSLog(@"NET_DVR_CaptureJPEGPicture failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_CaptureJPEGPicture succ");
    }
}
void Test_CaptureJPEGPicture_NEW(int iUserID, int iChan)
{
    NET_DVR_JPEGPARA struJpegPara = {0};
    struJpegPara.wPicSize = 2;
    
    char *pBuf = new char[2*1024*1024];
    memset(pBuf, 0, 2*1024*1024);
    DWORD dwRet = 0;
    if(!NET_DVR_CaptureJPEGPicture_NEW(iUserID, iChan, &struJpegPara, pBuf, 2*1024*1024, &dwRet))
    {
        NSLog(@"NET_DVR_CaptureJPEGPicture_NEW failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_CaptureJPEGPicture_NEW succ");
    }
    delete []pBuf;
    pBuf = NULL;
}

void fZeroChanPreviewCallBack(LONG lRealHandle, DWORD dwDataType, BYTE *pBuffer, DWORD dwBufSize, void* pUser)
{
    NSLog(@"fRealDataCallBack_V30 lRealHandle[%d], DataType[%d], BufSize[%d]", lRealHandle, dwDataType, dwBufSize);
}
void Test_ZeroChanPreview(int iUserID, int iChan)
{
    NET_DVR_CLIENTINFO ClientInfo = {0};
    ClientInfo.lChannel = iChan;
  
    int iRealPlayID = NET_DVR_ZeroStartPlay(iUserID, &ClientInfo, fZeroChanPreviewCallBack, NULL);
    if(iRealPlayID < 0)
    {
        NSLog(@"NET_DVR_ZeroStartPlay failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        sleep(10);
        NET_DVR_ZeroStopPlay(iRealPlayID);
    }
}
void Test_TransChannel(int iUserID)
{
    NET_DVR_SERIALSTART_V40 struSerial  = {0};
    struSerial.dwSize = sizeof(struSerial);
    struSerial.dwSerialType = 2;
    int iSerialHandle = NET_DVR_SerialStart_V40(iUserID, &struSerial, sizeof(struSerial), NULL, NULL);
    if(iSerialHandle >= 0)
    {
        NSLog(@"NET_DVR_SerialStart_V40 succ");
        if(!NET_DVR_SerialSend(iSerialHandle, 1, "11", 2))
        {
            NSLog(@"NET_DVR_SerialStart_V40 failed with[%d]", NET_DVR_GetLastError());
        }
        else
        {
            NSLog(@"NET_DVR_SerialStart_V40 succ");
        }
        NET_DVR_SerialStop(iSerialHandle);
    }
    else
    {
        NSLog(@"NET_DVR_SerialStart_V40 failed with[%d]", NET_DVR_GetLastError());
    }
    
}


void Test_DVRRecord(int iUserID, int iChan)
{
    if(!NET_DVR_StartDVRRecord(iUserID, iChan, 0))
    {
        NSLog(@"NET_DVR_StartDVRRecord failed with[%d]" , NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_StartDVRRecord succ!");
    }
    sleep(5);
    if(!NET_DVR_StopDVRRecord(iUserID, iChan))
    {
        NSLog(@"NET_DVR_StopDVRRecord failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_StopDVRRecord succ!");
    }
}


void Test_Serial(int iUserID)
{
    if(!NET_DVR_SendToSerialPort(iUserID, 1, 1, "11", 2))
    {
        NSLog(@"NET_DVR_SendToSerialPort failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_SendToSerialPort succ");
    }
    
    if(!NET_DVR_SendTo232Port(iUserID, "11", 2))
    {
        NSLog(@"NET_DVR_SendTo232Port failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_SendTo232Port succ");
    }
}


void Test_DVRMakeKeyFrame(int iUserID, int iChan)
{
    if(!NET_DVR_MakeKeyFrame( iUserID, iChan))
    {
        NSLog(@"NET_DVR_MakeKeyFrame failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_MakeKeyFrame succ!");
    }
}

void Test_DVRMakeKeyFrameSub(int iUserID, int iChan)
{
    if(!NET_DVR_MakeKeyFrameSub( iUserID, iChan))
    {
        NSLog(@"NET_DVR_MakeKeyFrameSub failed with[%d]", NET_DVR_GetLastError());
        
    }
    else
    {
        NSLog(@"NET_DVR_MakeKeyFrameSub succ!");
    }
}
void Test_SearchLog(int iUserID)
{
    NET_DVR_TIME struStartTime = {0};
    NET_DVR_TIME struEndTime = {0};
    struStartTime.dwYear = 2016;
    struStartTime.dwMonth = 6;
    struStartTime.dwDay = 28;
    struEndTime.dwYear = 2016;
    struEndTime.dwMonth = 6;
    struEndTime.dwDay = 29;
    int nSearchHandle = (int)NET_DVR_FindDVRLog_V30(iUserID, 0, 0, 0, &struStartTime, &struEndTime, false);
    if(nSearchHandle >= 0)
    {
        NET_DVR_LOG_V30	struLog = {0};
        int nState = -1;
        int nCount = 0;
        while(true)
        {
            nState = (int)NET_DVR_FindNextLog_V30((long)nSearchHandle, &struLog);
            if(nState != NET_DVR_FILE_SUCCESS && nState != NET_DVR_ISFINDING)
            {
                break;
            }
            else if(nState == NET_DVR_FILE_SUCCESS)
            {
                nCount++;
                NSLog(@"find log time: [%d]-[%d]-[%d]" ,struLog.strLogTime.dwHour ,struLog.strLogTime.dwMinute , struLog.strLogTime.dwSecond);
            }
            sleep(1);
        }
        NET_DVR_FindLogClose_V30(nSearchHandle);
    }
}

void Test_LoginMultiThread()
{
    int i = 0;
    for(i = 0; i < 100; i++)
    {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            NET_DVR_DEVICEINFO_V30 devInfo = {0};
            int lLoginID = NET_DVR_Login_V30("10.17.133.23", 8000, "admin", "12345", &devInfo);
            if(lLoginID >= 0)
            {
                NSLog(@"NET_DVR_Login_V30 succ[%d]", lLoginID);
            }
            else
            {
                NSLog(@"NET_DVR_Login_V30 failed[%d]", NET_DVR_GetLastError());
            }
            sleep(1);
            NET_DVR_Logout(lLoginID);
        });
    }
}

void Test_PlaybackSaveData(int iPlaybackID)
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *documentsDirectory = [paths objectAtIndex:0];
        char szFileName[256] = {0};
        NSString* date;
        NSDateFormatter* formatter = [[NSDateFormatter alloc]init];
        [formatter setDateFormat:@"YYYY-MM-dd-hh-mm-ss"];
        date = [formatter stringFromDate:[NSDate date]];
        sprintf(szFileName, "%s/%s.mp4", (char*)documentsDirectory.UTF8String, (char*)date.UTF8String);
        if(!NET_DVR_PlayBackSaveData(iPlaybackID, szFileName))
        {
            NSLog(@"NET_DVR_PlayBackSaveData failed[%d]", NET_DVR_GetLastError());
            return;
        }
        NSLog(@"NET_DVR_PlayBackSaveData succ[%s]", szFileName);
        sleep(10);
        if(!NET_DVR_StopPlayBackSave(iPlaybackID))
        {
            NSLog(@"NET_DVR_StopPlayBackSave falied[%d]", NET_DVR_GetLastError());
        }
        NSLog(@"NET_DVR_StopPlayBackSave succ");
    });
}

void Test_GetPlanList(int iUserID)
{   //4510
    NET_DVR_PLAN_LIST   struPlanList = {0};
    struPlanList.dwSize = sizeof(NET_DVR_PLAN_LIST);
    if(!NET_DVR_GetPlanList(iUserID, 0, &struPlanList))
    {
        NSLog(@"NET_DVR_GetPlanList falied[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_GetPlanList succ");
    }
}

void Test_GetInputSignalList_V40(int iUserID)
{
    //4510
    NET_DVR_INPUT_SIGNAL_LIST   struInputSignalList = {0};
    struInputSignalList.dwSize = sizeof(NET_DVR_INPUT_SIGNAL_LIST);
    
    if(!NET_DVR_GetInputSignalList(iUserID, 0, &struInputSignalList))
    {
        NSLog(@"NET_DVR_GetInputSignalList falied[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_GetInputSignalList succ");
    }
}

void test_TERMINAL_CONFERENCE_STATUS(int iUserID)
{
    NET_DVR_STD_CONFIG struPresetName = {0};
    NET_DVR_TERMINAL_CONFERENCE_STATUS OutBuffer = {0};
    OutBuffer.dwSize = sizeof(OutBuffer);
    
    struPresetName.lpCondBuffer = NULL;
    struPresetName.dwCondSize = 0;
    struPresetName.lpInBuffer = NULL;
    struPresetName.dwInSize = 0;
    struPresetName.lpOutBuffer = &OutBuffer;
    struPresetName.dwOutSize = sizeof(OutBuffer);
    struPresetName.lpStatusBuffer = NULL;
    struPresetName.dwStatusSize = 0;
    if(!NET_DVR_GetSTDConfig(iUserID, NET_DVR_GET_TERMINAL_CONFERENCE_STATUS, &struPresetName))
    {
        NSLog(@"NET_DVR_GET_TERMINAL_CONFERENCE_STATUS failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_GET_TERMINAL_CONFERENCE_STATUS succ");
    }
}

void test_CALLINFO_BY_COND(int iUserID)
{
    NET_DVR_STD_CONFIG struPresetName = {0};
    NET_DVR_CALL_QUERY_COND CondBuffer = {0};
    CondBuffer.dwSize = sizeof(CondBuffer);
    memcpy(CondBuffer.bySearchID, "1", MAX_SEARCH_ID_LEN);
    CondBuffer.struStartTime.dwYear = 2011;
    CondBuffer.struStartTime.dwMonth = 6;
    CondBuffer.struStartTime.dwDay = 23;
    CondBuffer.struStartTime.dwHour = 0;
    CondBuffer.struStartTime.dwMinute = 0;
    CondBuffer.struStartTime.dwSecond = 0;
    CondBuffer.struEndTime.dwYear = 2016;
    CondBuffer.struEndTime.dwMonth = 12;
    CondBuffer.struEndTime.dwDay = 23;
    CondBuffer.struEndTime.dwHour = 23;
    CondBuffer.struEndTime.dwMinute = 59;
    CondBuffer.struEndTime.dwSecond = 59;
    CondBuffer.byCallType = 1;
    CondBuffer.dwMaxResults = 1;
    CondBuffer.dwSearchPos = 1;
    NET_DVR_CALL_QUERY_RESULT OutBuffer = {0};
    OutBuffer.dwSize = sizeof(OutBuffer);
    
    struPresetName.lpCondBuffer = &CondBuffer;
    struPresetName.dwCondSize = sizeof(CondBuffer);
    struPresetName.lpInBuffer = NULL;
    struPresetName.dwInSize = 0;
    struPresetName.lpOutBuffer = &OutBuffer;
    struPresetName.dwOutSize = sizeof(OutBuffer);
    struPresetName.lpStatusBuffer = NULL;
    struPresetName.dwStatusSize = 0;
    if(!NET_DVR_GetSTDConfig(iUserID, NET_DVR_GET_CALLINFO_BY_COND, &struPresetName))
    {
        NSLog(@"NET_DVR_GET_CALLINFO_BY_COND failed with[%d]", NET_DVR_GetLastError());
    }
    else
    {
        NSLog(@"NET_DVR_GET_CALLINFO_BY_COND succ");
    }
}

void Test_ScreenFile(int iUserID)
{
    int lUploadHandle = -1;
    char szFileName[256] = {0};
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    sprintf(szFileName, "%s/test.gif", (char*)documentsDirectory.UTF8String); //手动修改文件名称
    
    NSLog(@"Test_ScreenFile file name[%s]", szFileName);
    
    DWORD uploadindex = 2;
    NET_DVR_SCREEM_FILE_UPLOAD_PARAM InBuffer = {0};
    InBuffer.dwSize = sizeof(InBuffer);
    InBuffer.byFileType  = 1;
    InBuffer.byPictureFormat = 4;
    memcpy(InBuffer.byFileName, "test.gif", strlen("test.gif"));
    lUploadHandle = NET_DVR_UploadFile_V40(iUserID, UPLOAD_SCREEN_FILE, &InBuffer, sizeof(InBuffer), szFileName, &uploadindex, 4);
    
    if(lUploadHandle >= 0)
    {
        NSLog(@"UPLOAD_SCREEN_FILE succ!");
        
        DWORD nProgress = -1;
        LONG nState = -1;
        
        while(true)
        {
            nState = NET_DVR_GetUploadState(lUploadHandle, &nProgress);
            NSLog(@"NET_DVR_GetUploadState err[%d]", NET_DVR_GetLastError());
            
            NSLog(@"progress[%d], state[%d]", nProgress, nState);
            if((int)nProgress < 0 || nProgress >= 100)
            {
                if(!NET_DVR_UploadClose(lUploadHandle))
                {
                    NSLog(@"NET_DVR_UploadClose failed[%d]", NET_DVR_GetLastError());
                }
                else
                {
                    NSLog(@"NET_DVR_UploadClose succ!");
                }
                break;
            }
            sleep(1);
        }
    }
    else
    {
        NSLog(@"UPLOAD_SCREEN_FILE failed[%d]", NET_DVR_GetLastError());
    }

}

void Test_Upgrade_V40(int iUserID)
{
    int lUpgradeHandle = -1;
    char szFileName[256] = {0};
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    sprintf(szFileName, "%s/digicap.dav", (char*)documentsDirectory.UTF8String);
    
    lUpgradeHandle = NET_DVR_Upgrade_V40(iUserID, /*ENUM_UPGRADE_LED*/0, szFileName, /*&ledType, 4*/NULL, 0);
    if (lUpgradeHandle != -1)
    {
        NSLog(@"NET_DVR_Upgrade_V40 succ!");
        
        int nProgress = -1;
        int nState = -1;
        int nStep = -1;
        int nSubProgress = -1;
        while(true)
        {
            nProgress = NET_DVR_GetUpgradeProgress(lUpgradeHandle);
            //NSLog(@"NET_DVR_GetUpgradeProgress err[%d]", NET_DVR_GetLastError());
            nState = NET_DVR_GetUpgradeState(lUpgradeHandle);
            //NSLog(@"NET_DVR_GetUpgradeState err[%d]", NET_DVR_GetLastError());
            nStep = NET_DVR_GetUpgradeStep(lUpgradeHandle, &nSubProgress);
            NSLog(@"NET_DVR_GetUpgradeStep err[%d]", NET_DVR_GetLastError());
            
            NSLog(@"progress[%d], state[%d], step[%d], subProgress[%d]", nProgress, nState, nStep, nSubProgress);
            if(nProgress < 0 || nProgress >= 100)
            {
                BOOL ret = 0;
                ret = NET_DVR_CloseUpgradeHandle(lUpgradeHandle);
                if (ret)
                {
                    NSLog(@"NET_DVR_CloseUpgradeHandle succ!");
                }
                else
                {
                    NSLog(@"NET_DVR_CloseUpgradeHandle failed[%d]", NET_DVR_GetLastError());
                }
                break;
            }
            sleep(1);
        }
    }
    else
    {
        NSLog(@"NET_DVR_Upgrade_V40 failed[%d]", NET_DVR_GetLastError());
    }
}

void TEST_Other(int iPreviewID, int iUserID, int iChan, int iPlaybackID)
{
//    Test_HikOnline();
//    Test_IPServer();
//    Test_CaptureJPEGPicture(iUserID, iChan);
//    Test_CaptureJPEGPicture_NEW(iUserID, iChan);
//    Test_ZeroChanPreview(iUserID, 1);
    Test_TransChannel(iUserID);
//    Test_Serial(iUserID);
//    Test_DVRMakeKeyFrame(iUserID, iChan);
//    Test_DVRMakeKeyFrameSub(iUserID, iChan);
//    Test_DVRRecord(iUserID, iChan);
//    Test_Activate();
//    Test_SearchLog(iUserID);
//    Test_LoginMultiThread();
//    Test_PlaybackSaveData(iPlaybackID);
//    Test_GetPlanList(iUserID);
//    Test_GetInputSignalList_V40(iUserID);
//    test_TERMINAL_CONFERENCE_STATUS(iUserID);
//    test_CALLINFO_BY_COND(iUserID);
//    Test_ScreenFile(iUserID);
//    Test_Upgrade_V40(iUserID);
}