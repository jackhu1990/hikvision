#ifndef __AudioBuffer__
#define __AudioBuffer__

#include <stdio.h>
#include <queue.h>
#include <pthread.h>


using std::queue;


#define PER_PACKAGE_MAX_SIZE  1536


typedef struct _AudioPackage
{
    char szDataBuf[PER_PACKAGE_MAX_SIZE];
    unsigned int nDataLen;
}AudioPackage;


class AudioBuffer
{
public:
    AudioBuffer();
    ~AudioBuffer();
    
    bool initBuffer(unsigned int bufferPackNum);
    
    void releaseBuffer();
    
    
    bool writePackage(unsigned char *dataBuf, unsigned int dataLen);
    
    bool readPackage(AudioPackage *outPackage);
    
private:
    
    queue<AudioPackage*> m_packageQueue;
    
    queue<AudioPackage*> m_idleQueue;
    
    pthread_mutex_t m_mutex;
};
    
    

#endif
