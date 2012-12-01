/*
 * WavMuxer.h
 *
 *  Created on: Nov 7, 2012
 *      Author: bigbug
 */

#ifndef AUDIO_MUXER_H_
#define AUDIO_MUXER_H_

#include "DependencyObject.h"
#include "Codecs.h"

class CAudioMuxer : public IAudioMuxer,
				    public CDependencyObject
{
public:
	CAudioMuxer();
	CAudioMuxer(IDependency* pDepend);
	virtual ~CAudioMuxer();

	// IWavMuxer
	virtual int CreateOutputContext(const char* pFileName, int nBitrate, int nSampleRate, int nChannels);
	virtual int DestroyOutputContext();
	virtual int Reset();
	virtual int WriteFileHeader();
	virtual int WriteFileData(BYTE* pbData, int nSize);
	virtual int WriteFileTrailer();

protected:
	int OpenAudio(AVCodec *codec);
	int WriteFileDataImpl();
	AVStream* AddAudioStream(AVCodec **ppCodec, AVCodecID eCodecID);

	int		m_nBitrate;
	int		m_nSampleRate;
	int 	m_nChannels;

	int		m_nFrameSize;
	int     m_nBufSize;
	BYTE*	m_pDstCur;
	BYTE*	m_pSamples;
	AVStream*	m_pAudioStream;

	AVOutputFormat*		m_pFmt;
	AVFormatContext*	m_pCtx;
};

#endif /* WAVMUXER_H_ */
