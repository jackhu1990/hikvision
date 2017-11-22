/*******************************************************
Copyright All Rights Reserved. (C) HangZhou Hikvision System Technology Co., Ltd. 
文  件：   PlayerBase.h
开发单位： 杭州海康威视    
编  写：   shiyanming
日  期：   2009-06-15
描  述：   declare player base definations
修  改： 
********************************************************/

#ifndef _PLAYER_BASE_H_
#define _PLAYER_BASE_H_

#include "HPR_Config.h"
#include "HPR_Types.h"


#define PLAYM4_MAX_SUPPORTS        500

typedef struct __RECT
{
    HPR_UINT32 nX;
    HPR_UINT32 nY;
    HPR_UINT32 nWidth;
    HPR_UINT32 nHeight;
}PLAYRECT;

#if defined(OS_APPLE)
typedef struct __INITINFO
{
    int uWidth;
    int uHeight;
}PLAY_INITINFO;

typedef void* HWND;

typedef struct __DC
{
    void*   surface;                        // SDL窗口的Surface
    HWND    hWnd;                           // HDC所在的窗口句柄
}DC;

typedef DC* HDC;

#elif defined OS_ANDROID

typedef struct __INITINFO
{
    int uWidth;
    int uHeight;
}PLAY_INITINFO;

typedef void* HWND;

typedef struct __DC
{
    void*   surface;                        // SDL窗口的Surface
    HWND    hWnd;                           // HDC所在的窗口句柄
}DC;

typedef DC* HDC;


#elif defined OS_POSIX

typedef struct __INITINFO
{
    int uWidth;
    int uHeight;
}PLAY_INITINFO;

#if defined(__linux__)
    typedef unsigned int HWND;
#else
    typedef void* HWND;
#endif

typedef struct __DC
{
    void*   surface;                        // SDL窗口的Surface
    HWND    hWnd;                           // HDC所在的窗口句柄
}DC;

typedef DC* HDC;

#endif

typedef union __PLAYHWND
{
#if defined(__linux__)
    HWND hWnd;            //for linux
#else
    HPR_HANDLE hWnd;    //for windows
#endif
    struct
    {
        PLAYRECT PlayRect;
        HPR_UINT32 nToScreen;
        HPR_UINT32 nToVideoOut;
    }RectHwnd;            //for linux and card
}PLAYHWND, *PPLAYHWND;

typedef enum __STREAM_MODE
{
    STREAM_REALTIME = 0,
    STREAM_FILE
}OPENSTREAM_MODE;

//frame type
#define T_AUDIO16    101
#define T_AUDIO8    100
#define T_UYVY        1
#define T_YV12        3
#define T_RGB32        7

typedef struct frameinfo
{
    HPR_INT32    width;
    HPR_INT32    height;
    HPR_INT32    stamp;
    HPR_INT32    type;
    HPR_INT32    frame_rate;
}FRAME_INFO;

typedef HPR_INT32 (CALLBACK *DECODECB)(HPR_VOIDPTR pbuffer, HPR_UINT32 nlength, FRAME_INFO *pframe_info, HPR_VOIDPTR puserdata);
typedef HPR_INT32 (CALLBACK *DISPLAYCB)(HPR_VOIDPTR pbuffer, HPR_UINT32 nlength, HPR_UINT32 nwidth, HPR_UINT32 nheight, HPR_UINT32 nstamp, HPR_UINT32 ntype, HPR_VOIDPTR puserdata);
typedef HPR_INT32 (CALLBACK *FILEENDCB)(HPR_VOIDPTR puserdata);

typedef struct  
{
    HPR_UINT16 nYear;
    HPR_UINT16 nMonth;
    HPR_UINT16 nDayOfWeek;
    HPR_UINT16 nDay;
    HPR_UINT16 nHour;
    HPR_UINT16 nMinute;
    HPR_UINT16 nSecond;
    HPR_UINT16 nMilliSeconds;
}SEGTIME, *PSEGTIME;

typedef HPR_INT32 (* STREAMDATACB)(HPR_VOIDPTR userdata, HPR_INT32 datatype, HPR_VOIDPTR pdata, HPR_INT32 datalen);


typedef void (CALLBACK *DRAWFUNCB)(HPR_INT32 lPlayHandle, HDC hDc, HPR_UINT32 nUser);

typedef HPR_UINT32   COLORKEY;
typedef HPR_UINT32   *LPCOLORKEY;


typedef struct{
    HPR_INT32 bToScreen; /* 是否输出到PC屏幕，1是0否 杭州海康威视数字技术有限公司版权所有 55
        Copyright(C)HangZhou Hikvision Digital Technology Co,.Ltd 2002-2008. All rights reserved.
        DS-40xxHC/HCS/HC+/HF/HS/MD 系列板卡 SDK 说明书 */
    HPR_INT32 bToVideoOut; /* 是否输出到监视器，1是0否（目前无效）*/
    HPR_INT32 nLeft; /* 输出到屏幕上的范围，相对hParent客户区坐标 */
    HPR_INT32 nTop;
    HPR_INT32 nWidth;
    HPR_INT32 nHeight;
    HPR_INT32 nReserved; /* 保留参数 */
}DISPLAY_PARA, *PDISPLAY_PARA;

typedef void (* DECODER_VIDEO_CAPTURE_CALLBACK)(HPR_UINT32 nChannelNumber, void *DataBuf, HPR_UINT32 width, HPR_UINT32 height,\
                            HPR_UINT32 nFrameNum, HPR_UINT32 nFrameTime, SEGTIME *pFrameAbsoluteTime, void *context);


//BUFFER TYPE (soft player)
#define BUF_VIDEO_SRC        1
#define BUF_AUDIO_SRC        2
#define BUF_VIDEO_RENDER    3
#define BUF_AUDIO_RENDER    4

typedef struct  //绝对时间 
{
    HPR_UINT32 dwYear; //年
    HPR_UINT32 dwMon; //月
    HPR_UINT32 dwDay; //日
    HPR_UINT32 dwHour; //时
    HPR_UINT32 dwMin; //分
    HPR_UINT32 dwSec; //秒
    HPR_UINT32 dwMs; //毫秒
}PLAYM4_SYSTEM_TIME, *PPLAYM4_SYSTEM_TIME;

#endif

