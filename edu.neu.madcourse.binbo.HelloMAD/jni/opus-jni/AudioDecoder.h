#ifndef AUDIO_DECODER_H_
#define AUDIO_DECODER_H_

#include "DependencyObject.h"
#include "Codecs.h"


class CAudioDecoder : public IAudioDecoder,
					  public CDependencyObject
{
public:
	CAudioDecoder();
	CAudioDecoder(IDependency* pDepend);
	virtual ~CAudioDecoder();

	// IAudioDecoder
	virtual int Create(const char* pszPath);
	virtual int Destroy();
	virtual int Reset();
	virtual int Decode(BYTE* pOutPCM, int nSize, int& nProgress);
	virtual int GetSampleRate(int* pSampleRate);
	virtual int GetChannels(int* pChannels);
	virtual int GetSampleFormat(int* pSampleFormat);
	virtual int GetBitrate(int* pBitrate);

protected:
	BOOL ReadPacket(AVPacket& rPacket);
	BOOL PrepareCodecs(AVFormatContext* pFmtCtx);
	BOOL PrepareAudioData(AVFormatContext* pFmtCtx);
	int DecodePacket(AVPacket& rPacket, int& nProgress);
	int UpdateProgress(AVPacket& rPacket);

	int		 m_nOffset;
	int		 m_nStreamID;
	int		 m_nCodecID;
	int      m_nSampleRate;
	int      m_nChannels;
	int		 m_nFrameSize;
	int		 m_nBitrate;
	int		 m_nSampleFormat;
	LONGLONG m_llDuration;
	float	 m_fTimebase;
	BOOL 	 m_bEOS;

	AVCodec*         m_pCodec;
	AVCodecContext*  m_pCodecCtx;
	AVFormatContext* m_pFormatCtx;
	AVPacket		 m_packet;

	int				 m_nPCMBufBeg;
	int				 m_nPCMBufEnd;
	BYTE 			 m_pPCMBuf[AVCODEC_MAX_AUDIO_FRAME_SIZE];
};

#endif
