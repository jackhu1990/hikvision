//
//  TestSwift.h
//  SimpleDemo
//
//  Created by Netsdk on 17/9/11.
//
//

#ifndef TestSwift_h
#define TestSwift_h

#import <Foundation/Foundation.h>


@interface OC_NET_DVR_DEVICEINFO_V30 : NSObject{
    NSString *sSerialNumber;
    Byte byAlarmInPortNum;
    Byte byAlarmOutPortNum;
    Byte byDiskNum;
    Byte byDVRType;
    Byte byChanNum;
    Byte byStartChan;
    Byte byAudioChanNum;
    Byte byIPChanNum;
    Byte byZeroChanNum;
    Byte byMainProto;
    Byte bySubProto;
    Byte bySupport;
    Byte bySupport1;
    Byte bySupport2;
    int16_t wDevType;
    Byte bySupport3;
    Byte byMultiStreamProto;
    Byte byStartDChan;
    Byte byStartDTalkChan;
    Byte byHighDChanNum;
    Byte bySupport4;
    Byte byLanguageType;
    Byte byVoiceInChanNum;
    Byte byStartVoiceInChanNo;
    Byte bySupport5;
    Byte bySupport6;
    Byte byMirrorChanNum;
    int16_t wStartMirrorChanNo;
    Byte bySupport7;
    Byte byRes2;
}

@property (nonatomic, retain) NSString *sSerialNumber;
@property Byte byAlarmInPortNum;
@property Byte byAlarmOutPortNum;
@property Byte byDiskNum;
@property Byte byDVRType;
@property Byte byChanNum;
@property Byte byStartChan;
@property Byte byAudioChanNum;
@property Byte byIPChanNum;
@property Byte byZeroChanNum;
@property Byte byMainProto;
@property Byte bySubProto;
@property Byte bySupport;
@property Byte bySupport1;
@property Byte bySupport2;
@property int16_t wDevType;
@property Byte bySupport3;
@property Byte byMultiStreamProto;
@property Byte byStartDChan;
@property Byte byStartDTalkChan;
@property Byte byHighDChanNum;
@property Byte bySupport4;
@property Byte byLanguageType;
@property Byte byVoiceInChanNum;
@property Byte byStartVoiceInChanNo;
@property Byte bySupport5;
@property Byte bySupport6;
@property Byte byMirrorChanNum;
@property int16_t wStartMirrorChanNo;
@property Byte bySupport7;
@property Byte byRes2;
@end

@interface OC_NET_DVR_PREVIEWINFO : NSObject{
    int32_t lChannel;
    int32_t dwStreamType;
    int32_t dwLinkMode;
    void * hPlayWnd;
    int32_t bBlocked;
    int32_t bPassbackRecord;
    Byte byPreviewMode;
    NSString *byStreamID;
    Byte byProtoType;
    Byte byRes1;
    Byte byVideoCodingType;
    int32_t dwDisplayBufNum;
    NSString *byRes;
}

@property int32_t lChannel;
@property int32_t dwStreamType;
@property int32_t dwLinkMode;
@property (nonatomic) void *hPlayWnd;
@property int32_t bBlocked;
@property int32_t bPassbackRecord;
@property Byte byPreviewMode;
@property (nonatomic, retain) NSString *byStreamID;
@property Byte byProtoType;
@property Byte byRes1;
@property Byte byVideoCodingType;
@property int32_t dwDisplayBufNum;
@property (nonatomic, retain) NSString *byRes;
@end

@interface OcObj : NSObject

-(int32_t) NET_DVR_Login_V30:(NSString*) sDVRIP wDVRPort:(UInt16)wDVRPort sUserName:(NSString*) sUserName sPassword:(NSString*)sPassword OC_LPNET_DVR_DEVICEINFO_V30:(OC_NET_DVR_DEVICEINFO_V30*)OC_LPNET_DVR_DEVICEINFO_V30;

-(BOOL)NET_DVR_Logout:(int)lUserID;

-(int32_t)NET_DVR_RealPlay_V40:(int)lUserID lpPreviewInfo:(OC_NET_DVR_PREVIEWINFO *)lpPreviewInfo;

-(BOOL)NET_DVR_StopRealPlay:(int)lRealHandle;

@end

#endif /* TestSwift_h */
