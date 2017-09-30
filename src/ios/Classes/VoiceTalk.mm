//
//  VoiceTalk.m
//  SimpleDemo
//
//  Created by Netsdk on 15/4/20.
//
//

#import <Foundation/Foundation.h>
#import "VoiceTalk.h"
#import "hcnetsdk.h"
#import "AudioEngine.h"
#import "AudioBuffer.h"
#import "AudioCodecDef.h"

#if !TARGET_IPHONE_SIMULATOR
CAudioEngine g_audioEngine(CAE_INTERCOM);
int g_iTalkID = -1;
AudioBuffer*  g_audioBuffer;



dispatch_source_t _sendTimer;

NSTimeInterval getTimerIntervalWithAudioCodecParam(AudioCodecParam* audioCodecparam)
{
    switch (audioCodecparam->enAudioEncodeType)
    {
        case AUDIO_TYPE_G722:
            //G722
            return 1.0f/25;
        case AUDIO_TYPE_G711A:
        case AUDIO_TYPE_G711U:
            //G711
            return 1.0f/50;
        case AUDIO_TYPE_G726:
            //G726
            return 1.0f/25;
        case AUDIO_TYPE_MPEG2:
            //MP2L2
        {
            switch (audioCodecparam->nSampleRate)
            {
                case AudioSampleRate16KHZ:
                    return 1.0f/13.89;
                case AudioSampleRate32KHZ:
                    return 1.0f/27.78;
                case AudioSampleRate44KHZ:
                    return 1.0f/38.28;
                case AudioSampleRate48KHZ:
                    return 1.0f/41.67;
                default:
                    return 1.0f/25;
            }
        }
        case AUDIO_TYPE_AAC:
            //AAC
        {
            switch (audioCodecparam->nSampleRate)
            {
                case AudioSampleRate16KHZ:
                    return 1.0f/16;
                case AudioSampleRate32KHZ:
                    return 1.0f/32;
                case AudioSampleRate44KHZ:
                    return 1.0f/44;
                case AudioSampleRate48KHZ:
                    return 1.0f/48;
                default:
                    return 1.0f/25;
            }
            
        }
        default:
            return 1.0f/25;
    }
}

void timeOutOfSendTimer()
{
        if (g_iTalkID >= 0)
        {
            if (g_audioBuffer) {
                AudioPackage _outPackage;
                if (g_audioBuffer->readPackage(&_outPackage))
                {
                    if (!NET_DVR_VoiceComSendData(g_iTalkID, (char*)_outPackage.szDataBuf, _outPackage.nDataLen))
                    {
                        //   NSLog(@"NET_DVR_VoiceComSendData failed! Error:%d", NET_DVR_GetLastError());
                    }
                }
                else
                {
                    //   NSLog(@"_audioBuffer->readPackage failed!");
                }
            }

        }

}


void fVoiceTalkRecordCallBack(OutputDataInfo* pstDataInfo, void* pUser)
{
    
    if (g_iTalkID >= 0 )
    {
        if (g_audioBuffer) {
            //read to buffer
            if (!g_audioBuffer->writePackage(pstDataInfo->pData, pstDataInfo->dwDataLen))
            {
                NSLog(@"_audioBuffer->writePackage failed!");
            }
        }

    }

}
void fVoiceTalkPlayCallBack(LONG iTalkID, char *pBuff, DWORD dwBufLen, BYTE byAudioFlag, void *pUser)
{
    g_audioEngine.InputData((BYTE*)pBuff, dwBufLen);
}
BOOL startAudioEngine(AudioCodecParam *pAuidoParam)
{
    //audio engine open
    int iRet = g_audioEngine.Open();
    if(iRet != 0)
    {
        NSLog(@"CAudioEngine open failed[%d]", iRet);
        return FALSE;
    }
    //set parameter
    iRet = g_audioEngine.SetAudioParam(pAuidoParam, PARAM_MODE_PLAY);
    if(iRet != 0)
    {
        g_audioEngine.Close();
        NSLog(@"CAudioEngine SetAudioParam PARAM_MODE_PLAY failed[%d]", iRet);
        return FALSE;
    }
    iRet = g_audioEngine.SetAudioParam(pAuidoParam, PARAM_MODE_RECORD);
    if(iRet != 0)
    {
        g_audioEngine.Close();
        NSLog(@"CAudioEngine SetAudioParam PARAM_MODE_RECORD failed[%d]", iRet);
        return FALSE;
    }
    g_audioEngine.OpenAEC(true);
    //set callback
    iRet = g_audioEngine.SetAudioDataCallBack(fVoiceTalkRecordCallBack, RECORD_DATA_CALLBACK, NULL);
    if(iRet != 0)
    {
        g_audioEngine.Close();
        NSLog(@"CAudioEngine SetAudioDataCallBack failed[%d]", iRet);
        return FALSE;
    }
    iRet = g_audioEngine.StartPlay();
    if(iRet != 0)
    {
        g_audioEngine.Close();
        NSLog(@"CAudioEngine StartPlay failed[%d]", iRet);
        return FALSE;
    }
    iRet = g_audioEngine.StartRecord();
    if(iRet != 0)
    {
        g_audioEngine.Close();
        g_audioEngine.StopPlay();
        NSLog(@"CAudioEngine StartRecord failed[%d]", iRet);
        return FALSE;
    }
    return TRUE;
}

int startVoiceTalk(int iUserID)
{
    NET_DVR_COMPRESSION_AUDIO   compressAudio = {0};
    if(!NET_DVR_GetCurrentAudioCompress(iUserID, &compressAudio))
    {
        NSLog(@"NET_DVR_GetCurrentAudioCompress failed[%d]", NET_DVR_GetLastError());
        return -1;
    }
    
    AudioCodecParam struAudioParam ;
    
    if(compressAudio.byAudioEncType == 1)//G711_U
    {
        struAudioParam.enAudioEncodeType = AUDIO_TYPE_G711U;
        struAudioParam.nBitWidth = 16;
        struAudioParam.nSampleRate = 8000;
        struAudioParam.nChannel = 1;
        struAudioParam.nBitRate = 16000;
    }
    else if(compressAudio.byAudioEncType == 2)//G711_A
    {
        struAudioParam.enAudioEncodeType = AUDIO_TYPE_G711A;
        struAudioParam.nBitWidth = 16;
        struAudioParam.nSampleRate = 8000;
        struAudioParam.nChannel = 1;
        struAudioParam.nBitRate = 16000;
    }
    else if(compressAudio.byAudioEncType == 0)//G722
    {
        struAudioParam.enAudioEncodeType = AUDIO_TYPE_G722;
        struAudioParam.nBitWidth = 16;
        struAudioParam.nSampleRate = 16000;
        struAudioParam.nChannel = 1;
        struAudioParam.nBitRate = 16000;
    }
    else
    {
        NSLog(@"the device audio type is not support by AudioEngineSDK for ios,type:%d", compressAudio.byAudioEncType);
        return -1;
    }
    //start audioengine
    if(!startAudioEngine(&struAudioParam))
    {
        return  -1;
    }
    
    g_audioBuffer = new AudioBuffer();
    //init audio buffer
    g_audioBuffer->initBuffer(50);
    
    //start hcnetsdk
    g_iTalkID = NET_DVR_StartVoiceCom_MR_V30(iUserID, 1, fVoiceTalkPlayCallBack, NULL);
    if(g_iTalkID < 0)
    {
        stopVoiceTalk();
        NSLog(@"NET_DVR_StartVoiceCom_MR_V30 falied[%d]", NET_DVR_GetLastError());
        return -1;
    }
    
    //start time dispatch
    _sendTimer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0));

    
    NSTimeInterval iTime = getTimerIntervalWithAudioCodecParam(&struAudioParam);
    dispatch_source_set_timer(_sendTimer, DISPATCH_TIME_NOW, iTime * NSEC_PER_SEC, 0.0);
    dispatch_source_set_event_handler(_sendTimer, ^{
        timeOutOfSendTimer();
    });
    
    dispatch_resume(_sendTimer);
    return  g_iTalkID;
}
void stopVoiceTalk()
{
    //stop time dispatch
    if (_sendTimer)
    {
        dispatch_source_cancel(_sendTimer);
        _sendTimer = nil;
    }
    NET_DVR_StopVoiceCom(g_iTalkID);
    g_iTalkID = -1;
    g_audioBuffer->releaseBuffer();
    delete g_audioBuffer;
    g_audioBuffer = nil;
    g_audioEngine.StopRecord();
    g_audioEngine.StopPlay();
    g_audioEngine.Close();
}

#endif
