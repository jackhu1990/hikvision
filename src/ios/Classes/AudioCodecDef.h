#ifndef _AUDIO_CODEC_DEF_H_
#define _AUDIO_CODEC_DEF_H_


//音频编码类型
#define AUDIO_CODEC_G722    0
#define AUDIO_CODEC_G711_U  1
#define AUDIO_CODEC_G711_A  2
#define AUDIO_CODEC_G726    6
#define AUDIO_CEDEC_MP2L2   5
#define AUDIO_CEDEC_AAC     7
#define AUDIO_CODEC_PCM     8

//音频采样枚举
enum
{
    AudioSampleRateDefault = 0,
    AudioSampleRate16 = 1,
    AudioSampleRate32 = 2,
    AudioSampleRate48 = 3,
    AudioSampleRate44 = 4,
    AudioSampleRate8 = 5
};

//音频采样格式枚举
enum
{
    AudioSampleRateDefaultKHZ= 16000,
    AudioSampleRate16KHZ = 16000,
    AudioSampleRate32KHZ = 32000,
    AudioSampleRate48KHZ = 48000,
    AudioSampleRate44KHZ = 44100,
    AudioSampleRate8KHZ = 8000
    
};

//音频比特率枚举
enum
{
    BitRateEncodeDefault = 16000,
    BitRateEncode8Kps = 8000,
    BitRateEncode16Kps = 16000,
    BitRateEncode32Kps = 32000,
    BitRateEncode64Kps = 64000,
    BitRateEncode128Kps = 128000,
    BitRateEncode192Kps = 192000,
    BitRateEncode40Kps = 40000,
    BitRateEncode48Kps = 48000,
    BitRateEncode56Kps = 56000,
    BitRateEncode80Kps = 80000,
    BitRateEncode96Kps = 96000,
    BitRateEncode112Kps = 112000,
    BitRateEncode144Kps = 144000,
    BitRateEncode160Kps = 160000
};

//音频采样格式
#define AUDIO_SAMPLE_RATE_G722  16000   //G722采样率
#define AUDIO_SAMPLE_RATE_G711  8000    //G711采样率
#define AUDIO_SAMPLE_RATE_G726  8000    //G726采样率
#define AUDIO_SAMPLE_RATE_MP2L2 16000   //MP2L2采样率
#define AUDIO_SAMPLE_RATE_AAC    16000  //AAC采样率
#define AUDIO_SAMPLE_RATE_PCM    16000  //PCM采样率


#define AUDIO_BITS_PER_SAMPLE 16        //采样大小
#define AUDIO_CHANNEL_NUMBER  1         //声道数

#define AUDIO_BITRATE_G722   16000     //G722比特率
#define AUDIO_BITRATE_G711   64000     //G711比特率
#define AUDIO_BITRATE_G726   16000     //G726比特率
#define AUDIO_BITRATE_MP2L2  64000     //MP2L2比特率
#define AUDIO_BITRATE_AAC    32000     //AAC比特率
#define AUDIO_BITRATE_PCM    8000      //PCM比特率


#define G722_FRAME_SIZE     80    //G722帧长度
#define G722_DEC_FRAME_SIZE  1280  //G722解码后长度

#define G711_FRAME_SIZE		160   //G711帧长度   2012-02-22 最新的G711支持160解码
#define G711_DEC_FRAME_SIZE   320   //G711解码后长度

#define G726_FRAME_SIZE     80    //G726帧长度
#define G726_DEC_FRAME_SIZE   640   //G726解码后长度

//#define MP2L2_FRAME_SIZE    576    //MP2L2帧长度  128000时是1152
//#define AAC_MAX_FRAME_SIZE      788    //AAC帧长度 (不定长，最大788)

// wuyag add
#define WAVE_FORMAT_PCM      1

//MP2L2 32000、64000、128000时，语音对讲库都是每秒回调15次左右，回调数据大小分别是288、576、1152



#endif // _AUDIO_CODEC_DEF_H_
