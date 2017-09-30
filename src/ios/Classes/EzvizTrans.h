#ifndef _HC_EZVIZ_SDK_H_
#define _HC_EZVIZ_SDK_H_
#include "HCNetSDK.h"

//萤石错误码 从5001~6000
#define NET_DVR_EZVIZ_NO_ENOUGH_DATA                                    5001  //接收的数据长度不够
#define NET_DVR_EZVIZ_SDK_ERROR                                         5002  //民用通信库加载失败 
#define NET_DVR_EZVIZ_GENERAL_UNKNOW_ERROR                              5003  //未知错误
#define NET_DVR_EZVIZ_GENERAL_SERIAL_NOT_FOR_CIVIL                      5004  //序列号解析失败                               
#define NET_DVR_EZVIZ_GENERAL_SERIAL_FORBIDDEN                          5005  //序列号被禁止
#define NET_DVR_EZVIZ_GENERAL_SERIAL_DUPLICATE                          5006  //序列号重复      
#define NET_DVR_EZVIZ_GENERAL_SERIAL_FLUSHED_IN_A_SECOND                5007  //相同序列号短时间内大量请求
#define NET_DVR_EZVIZ_GENERAL_SERIAL_NO_LONGER_SUPPORTED                5008  //序列号不再支持

#define NET_DVR_EZVIZ_PLATFORM_CLIENT_REQUEST_NO_PU_FOUNDED             5009  //请求的设备不在线        
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_REQUEST_REFUSED_TO_PROTECT_PU     5010  //为了保护设备，拒绝请求
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_REQUEST_PU_LIMIT_REACHED          5011  //设备达到链接的客户端上限
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_TEARDOWN_PU_CONNECTION            5012  //服务器要求客户端断开与设备的连接
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_VERIFY_SESSION_ERROR              5013  //设备拒绝平台发送的客户端连接请求  
#define NET_DVR_EZVIZ_PLAYFORM_VERIFY_DATA_ERROR                        5014  //萤石平台校验客户端发过来的数据非法

#define NET_DVR_EZVIZ_OPEN_PLAYFORM_VERIFY_DATA_ERROR                   6001  //萤石开放平台校验客户端发过来的参数错误
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ACCESSTOCKEN_ERROR                  6002  //萤石开放平台校验access_tocken异常
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ACCESSTOCKEN_OVERDUE                6003  //萤石开放平台校验access_tocken过期
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_APPKEY_ERROR                        6005  //萤石开放平台校验appKey异常
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NO_CHANNEL_FOUNDED                  7001  //萤石开放平台校验通道不存在
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NO_PU_FOUNDED                       7002  //萤石开放平台校验设备不存在
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_FEATURECODE_NULL                    7003  //萤石开放平台校验硬件特征码为空，版本过低
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_VERIFY_FEATURECODE_FAILED           7004  //萤石开放平台校验硬件特征码检测失败，版本过低
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_OPERATE_FEATURECODE_FAILED          7005  //萤石开放平台硬件特征码操作失败
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NETWORK_ERROR                       7006  //萤石开放平台网络异常
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_DEVICE_NOT_ONLINE                   7007  //萤石开放平台校验设备不在线
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_DEVICE_TIMEOUT                      7008  //萤石开放平台设备响应超时
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ERROR                               7500  //萤石开放平台服务器异常

#define EZVIZ_CLASSSESSION_LEN  64
#define EZVIZ_DEVICEID_LEN      32

#define NET_DVR_DEV_ADDRESS_MAX_LEN  129
#define EZVIZ_ACCESSTOKEN_LEN  128
#define EZVIZ_REQURL_LEN		64
#define EZVIZ_CLIENTTYPE_LEN	32
#define EZVIZ_FEATURECODE_LEN	64
#define EZVIZ_OSVERSION_LEN		32
#define EZVIZ_NETTYPE_LEN		32
#define EZVIZ_SDKVERSION_LEN	32
#define EZVIZ_APPID_LEN			64


typedef struct  tagNET_DVR_EZVIZ_USER_LOGIN_INFO
{
    char sEzvizServerAddress[NET_DVR_DEV_ADDRESS_MAX_LEN]; //云服务器地址 
    WORD wPort;       //云服务器端口
    BYTE byRes1[2];  
    char sClassSession[EZVIZ_CLASSSESSION_LEN];  //ClassSession, 服务器分配的一个字符串，每次通信时需要发送给服务器
    char sDeviceID[EZVIZ_DEVICEID_LEN];          //设备ID， 由服务器分配的     
    BYTE byRes2[128];
}NET_DVR_EZVIZ_USER_LOGIN_INFO,*LPNET_DVR_EZVIZ_USER_LOGIN_INFO;

typedef struct  tagNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO
{
    char sEzvizServerAddress[NET_DVR_DEV_ADDRESS_MAX_LEN]; //云服务器地址       open.ys7.com
    BYTE byRes1[3];
    WORD wPort;       //云服务器端口
    BYTE byRes2[2];
    char sUrl[EZVIZ_REQURL_LEN];           //  /api/device/transmission
    char sAccessToken[EZVIZ_ACCESSTOKEN_LEN];  //ClassSession, 服务器分配的一个字符串，每次通信时需要发送给服务器
    char sDeviceID[EZVIZ_DEVICEID_LEN];          //设备ID， 由服务器分配的
    char sClientType[EZVIZ_CLIENTTYPE_LEN];		//客户端类型: 0: PC-控件 1: ios 2: android
    char sFeatureCode[EZVIZ_FEATURECODE_LEN];	//硬件特征码
    char sOsVersion[EZVIZ_OSVERSION_LEN];		//终端系统版本, 例如: IOS 7.0.4, Android 2.3.
    char sNetType[EZVIZ_NETTYPE_LEN];			//网络类型, UNKNOWN GPRS EDGE UMTS HSDPA HSUPA HSPA CDMAEVDO_0 EVDO_A EVDO_B 1xRTT IDEN WIFI
    char sSdkVersion[EZVIZ_SDKVERSION_LEN];		//Sdk版本号, v.1.0.20140720.45xx
    char sAppID[EZVIZ_APPID_LEN];				//AppID，ios上报BundleID，Android上报包名
    BYTE byRes3[512];
}NET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO,*LPNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO;

NET_DVR_API LONG __stdcall NET_DVR_CreateEzvizUser(LPNET_DVR_EZVIZ_USER_LOGIN_INFO pLoginInfo, LPNET_DVR_DEVICEINFO_V30 pDeviceInfo);
NET_DVR_API BOOL __stdcall NET_DVR_DeleteEzvizUser(LONG lUserID);

NET_DVR_API LONG __stdcall NET_DVR_CreateOpenEzvizUser(LPNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO pLoginInfo, LPNET_DVR_DEVICEINFO_V30 pDeviceInfo);
NET_DVR_API BOOL __stdcall NET_DVR_DeleteOpenEzvizUser(LONG lUserID);


#endif
