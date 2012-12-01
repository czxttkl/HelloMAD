#ifndef SYS_CODECS_H_
#define SYS_CODECS_H_

#ifdef __cplusplus
extern "C" {
#endif

#include "libavformat/avformat.h"
#include "libavutil/mathematics.h"
#include "libavcodec/avcodec.h"
#include "libswresample/swresample.h"

#ifdef __cplusplus
}
#endif

struct IAudioEncoder
{
	virtual int Create(int nBitrate, int nSampleRate, int nChannels, int nApplication, const char* pszPath) = 0;
	virtual int Destroy() = 0;
	virtual int Reset() = 0;
	virtual int Encode(BYTE* pRaw, int nSize) = 0;
};

struct IAudioDecoder
{
	virtual int Create(const char* pszPath) = 0;
	virtual int Destroy() = 0;
	virtual int Reset() = 0;
	virtual int Decode(BYTE* pPCM, int nSize, int& nProgress) = 0;
	virtual int GetSampleRate(int* pSampleRate) = 0;
	virtual int GetChannels(int* pChannels) = 0;
	virtual int GetSampleFormat(int* pSampleFormat) = 0;
	virtual int GetBitrate(int* pBitrate) = 0;
};

struct IAudioMuxer
{
	virtual int CreateOutputContext(const char* pFileName, int nBitrate, int nSampleRate, int nChannels) = 0;
	virtual int DestroyOutputContext() = 0;
	virtual int Reset() = 0;
	virtual int WriteFileHeader() = 0;
	virtual int WriteFileData(BYTE* pbData, int nSize) = 0;
	virtual int WriteFileTrailer() = 0;
};


#endif
