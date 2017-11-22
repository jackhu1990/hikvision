#ifndef _HC_EZVIZ_SDK_H_
#define _HC_EZVIZ_SDK_H_
#include "HCNetSDK.h"

//өʯ������ ��5001~6000
#define NET_DVR_EZVIZ_NO_ENOUGH_DATA                                    5001  //���յ����ݳ��Ȳ���
#define NET_DVR_EZVIZ_SDK_ERROR                                         5002  //����ͨ�ſ����ʧ�� 
#define NET_DVR_EZVIZ_GENERAL_UNKNOW_ERROR                              5003  //δ֪����
#define NET_DVR_EZVIZ_GENERAL_SERIAL_NOT_FOR_CIVIL                      5004  //���кŽ���ʧ��                               
#define NET_DVR_EZVIZ_GENERAL_SERIAL_FORBIDDEN                          5005  //���кű���ֹ
#define NET_DVR_EZVIZ_GENERAL_SERIAL_DUPLICATE                          5006  //���к��ظ�      
#define NET_DVR_EZVIZ_GENERAL_SERIAL_FLUSHED_IN_A_SECOND                5007  //��ͬ���кŶ�ʱ���ڴ�������
#define NET_DVR_EZVIZ_GENERAL_SERIAL_NO_LONGER_SUPPORTED                5008  //���кŲ���֧��

#define NET_DVR_EZVIZ_PLATFORM_CLIENT_REQUEST_NO_PU_FOUNDED             5009  //������豸������        
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_REQUEST_REFUSED_TO_PROTECT_PU     5010  //Ϊ�˱����豸���ܾ�����
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_REQUEST_PU_LIMIT_REACHED          5011  //�豸�ﵽ���ӵĿͻ�������
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_TEARDOWN_PU_CONNECTION            5012  //������Ҫ��ͻ��˶Ͽ����豸������
#define NET_DVR_EZVIZ_PLAYFORM_CLIENT_VERIFY_SESSION_ERROR              5013  //�豸�ܾ�ƽ̨���͵Ŀͻ�����������  
#define NET_DVR_EZVIZ_PLAYFORM_VERIFY_DATA_ERROR                        5014  //өʯƽ̨У��ͻ��˷����������ݷǷ�

#define NET_DVR_EZVIZ_OPEN_PLAYFORM_VERIFY_DATA_ERROR                   6001  //өʯ����ƽ̨У��ͻ��˷������Ĳ�������
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ACCESSTOCKEN_ERROR                  6002  //өʯ����ƽ̨У��access_tocken�쳣
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ACCESSTOCKEN_OVERDUE                6003  //өʯ����ƽ̨У��access_tocken����
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_APPKEY_ERROR                        6005  //өʯ����ƽ̨У��appKey�쳣
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NO_CHANNEL_FOUNDED                  7001  //өʯ����ƽ̨У��ͨ��������
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NO_PU_FOUNDED                       7002  //өʯ����ƽ̨У���豸������
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_FEATURECODE_NULL                    7003  //өʯ����ƽ̨У��Ӳ��������Ϊ�գ��汾����
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_VERIFY_FEATURECODE_FAILED           7004  //өʯ����ƽ̨У��Ӳ����������ʧ�ܣ��汾����
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_OPERATE_FEATURECODE_FAILED          7005  //өʯ����ƽ̨Ӳ�����������ʧ��
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_NETWORK_ERROR                       7006  //өʯ����ƽ̨�����쳣
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_DEVICE_NOT_ONLINE                   7007  //өʯ����ƽ̨У���豸������
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_DEVICE_TIMEOUT                      7008  //өʯ����ƽ̨�豸��Ӧ��ʱ
#define NET_DVR_EZVIZ_OPEN_PLAYFORM_ERROR                               7500  //өʯ����ƽ̨�������쳣

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
    char sEzvizServerAddress[NET_DVR_DEV_ADDRESS_MAX_LEN]; //�Ʒ�������ַ 
    WORD wPort;       //�Ʒ������˿�
    BYTE byRes1[2];  
    char sClassSession[EZVIZ_CLASSSESSION_LEN];  //ClassSession, �����������һ���ַ�����ÿ��ͨ��ʱ��Ҫ���͸�������
    char sDeviceID[EZVIZ_DEVICEID_LEN];          //�豸ID�� �ɷ����������     
    BYTE byRes2[128];
}NET_DVR_EZVIZ_USER_LOGIN_INFO,*LPNET_DVR_EZVIZ_USER_LOGIN_INFO;

typedef struct  tagNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO
{
    char sEzvizServerAddress[NET_DVR_DEV_ADDRESS_MAX_LEN]; //�Ʒ�������ַ       open.ys7.com
    BYTE byRes1[3];
    WORD wPort;       //�Ʒ������˿�
    BYTE byRes2[2];
    char sUrl[EZVIZ_REQURL_LEN];           //  /api/device/transmission
    char sAccessToken[EZVIZ_ACCESSTOKEN_LEN];  //ClassSession, �����������һ���ַ�����ÿ��ͨ��ʱ��Ҫ���͸�������
    char sDeviceID[EZVIZ_DEVICEID_LEN];          //�豸ID�� �ɷ����������
    char sClientType[EZVIZ_CLIENTTYPE_LEN];		//�ͻ�������: 0: PC-�ؼ� 1: ios 2: android
    char sFeatureCode[EZVIZ_FEATURECODE_LEN];	//Ӳ��������
    char sOsVersion[EZVIZ_OSVERSION_LEN];		//�ն�ϵͳ�汾, ����: IOS 7.0.4, Android 2.3.
    char sNetType[EZVIZ_NETTYPE_LEN];			//��������, UNKNOWN GPRS EDGE UMTS HSDPA HSUPA HSPA CDMAEVDO_0 EVDO_A EVDO_B 1xRTT IDEN WIFI
    char sSdkVersion[EZVIZ_SDKVERSION_LEN];		//Sdk�汾��, v.1.0.20140720.45xx
    char sAppID[EZVIZ_APPID_LEN];				//AppID��ios�ϱ�BundleID��Android�ϱ�����
    BYTE byRes3[512];
}NET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO,*LPNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO;

NET_DVR_API LONG __stdcall NET_DVR_CreateEzvizUser(LPNET_DVR_EZVIZ_USER_LOGIN_INFO pLoginInfo, LPNET_DVR_DEVICEINFO_V30 pDeviceInfo);
NET_DVR_API BOOL __stdcall NET_DVR_DeleteEzvizUser(LONG lUserID);

NET_DVR_API LONG __stdcall NET_DVR_CreateOpenEzvizUser(LPNET_DVR_OPEN_EZVIZ_USER_LOGIN_INFO pLoginInfo, LPNET_DVR_DEVICEINFO_V30 pDeviceInfo);
NET_DVR_API BOOL __stdcall NET_DVR_DeleteOpenEzvizUser(LONG lUserID);


#endif