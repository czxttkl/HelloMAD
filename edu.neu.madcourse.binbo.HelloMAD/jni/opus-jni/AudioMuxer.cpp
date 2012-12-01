/*
 * WavMuxer.cpp
 *
 *  Created on: Nov 7, 2012
 *      Author: bigbug
 */
#include "AudioMuxer.h"

CAudioMuxer::CAudioMuxer(IDependency* pDepend)
	: CDependencyObject(pDepend)
{
	m_pCtx = NULL;
	m_pFmt = NULL;

	m_pSamples = NULL;

	Reset();

	av_register_all();
}

CAudioMuxer::~CAudioMuxer()
{
	DestroyOutputContext();
}

int CAudioMuxer::Reset()
{
	m_nBufSize = 0;
	m_nFrameSize = 0;

	m_nBitrate    = 12000;
	m_nSampleRate = 16000;
	m_nChannels	  = 2;

	return S_OK;
}

AVStream* CAudioMuxer::AddAudioStream(AVCodec **ppCodec, AVCodecID eCodecID)
{
	AVCodecContext *c;
	AVStream *st;

	/* find the audio encoder */
	*ppCodec = avcodec_find_encoder(eCodecID);
	if (!(*ppCodec)) {
		Log("Could not find codec\n");
		return NULL;
	}

	st = avformat_new_stream(m_pCtx, *ppCodec);
	if (!st) {
		Log("Could not allocate stream\n");
		return NULL;
	}
	st->id = 1;

	c = st->codec;

	/* put sample parameters */
	c->sample_fmt  = AV_SAMPLE_FMT_S16;
	c->bit_rate    = m_nBitrate;
	c->sample_rate = m_nSampleRate;
	c->channels    = m_nChannels;

	// some formats want stream headers to be separate
	if (m_pFmt->flags & AVFMT_GLOBALHEADER)
		c->flags |= CODEC_FLAG_GLOBAL_HEADER;

	return st;
}

int CAudioMuxer::OpenAudio(AVCodec *codec)
{
	AVCodecContext *c;

	c = m_pAudioStream->codec;

	/* open it */
	if (avcodec_open2(c, codec, NULL) < 0) {
		Log("could not open codec\n");
		return E_FAIL;
	}

	if (c->codec->capabilities & CODEC_CAP_VARIABLE_FRAME_SIZE) {
		m_nFrameSize = 10000;
	} else {
		m_nFrameSize = c->frame_size;
	}

	m_nBufSize = m_nFrameSize * av_get_bytes_per_sample(c->sample_fmt) * c->channels;
	m_pSamples = (BYTE*)av_malloc(m_nBufSize);
	m_pDstCur = m_pSamples;

	return S_OK;
}

int CAudioMuxer::CreateOutputContext(const char* pFileName, int nBitrate, int nSampleRate, int nChannels)
{
	// avoid re-creating the context
	if (m_pCtx) {
		return E_HANDLED;
	}

	/* allocate the output media context */
	avformat_alloc_output_context2(&m_pCtx, NULL, NULL, pFileName);
	if (!m_pCtx) {
		Log("Could not deduce output format from file extension: using MPEG.\n");
		avformat_alloc_output_context2(&m_pCtx, NULL, "mpeg", pFileName);
		if (!m_pCtx) {
			return E_FAIL;
		}
	}
	m_pFmt = m_pCtx->oformat;

	// set the input bitrate, sample rate and channels
	m_nBitrate    = nBitrate;
	m_nSampleRate = nSampleRate;
	m_nChannels   = nChannels;

	/* Add the audio and video streams using the default format codecs
	 * and initialize the codecs. */
	AVCodec *pAudioCodec;
	m_pAudioStream = NULL;

	AVCodecID eCodecID = m_pFmt->audio_codec;
	if (eCodecID != AV_CODEC_ID_NONE) {
		m_pAudioStream = AddAudioStream(&pAudioCodec, eCodecID);
	}

	/* Now that all the parameters are set, we can open the audio and
	 * video codecs and allocate the necessary encode buffers. */
	if (m_pAudioStream) {
		OpenAudio(pAudioCodec);
	}

	/* open the output file, if needed */
	if (!(m_pFmt->flags & AVFMT_NOFILE)) {
		if (avio_open(&m_pCtx->pb, pFileName, AVIO_FLAG_WRITE) < 0) {
			Log("Could not open '%s'\n", pFileName);
			return E_IO;
		}
	}

	return S_OK;
}

int CAudioMuxer::DestroyOutputContext()
{
	if (m_pSamples) {
		av_free(m_pSamples);
		m_pSamples = NULL;
	}

	/* Close each codec. */
	if (m_pAudioStream) {
		avcodec_close(m_pAudioStream->codec);
		m_pAudioStream = NULL;
	}

	/* Free the streams. */
	if (m_pCtx) {
		for (int i = 0; i < m_pCtx->nb_streams; i++) {
			if (m_pCtx->streams[i]->codec) {
				av_freep(&m_pCtx->streams[i]->codec);
			}
			if (m_pCtx->streams[i]) {
				av_freep(&m_pCtx->streams[i]);
			}
		}
		if (m_pCtx->pb) {
			avio_close(m_pCtx->pb);
		}
		av_free(m_pCtx);
		m_pCtx = NULL;
	}

	Reset();

	return S_OK;
}

int CAudioMuxer::WriteFileHeader()
{
	/* Write the stream header, if any. */
	if (avformat_write_header(m_pCtx, NULL) < 0) {
		Log("Error occurred when opening output file\n");
		return E_IO;
	}

	return S_OK;
}

int CAudioMuxer::WriteFileData(BYTE* pbData, int nSize)
{
	BYTE* pSrcCur = pbData;
	BYTE* pSrcEnd = pbData + nSize;
	BYTE* pDstEnd = m_pSamples + m_nBufSize;

	while (nSize > 0) {

		if (nSize < pDstEnd - m_pDstCur) {
			memcpy(m_pDstCur, pSrcCur, nSize);
			m_pDstCur += nSize;
			pSrcCur   += nSize;
			AssertValid(pSrcCur == pSrcEnd);
		} else {
			memcpy(m_pDstCur, pSrcCur, pDstEnd - m_pDstCur);
			pSrcCur += pDstEnd - m_pDstCur;
			WriteFileDataImpl();
			m_pDstCur = m_pSamples;
		}
		nSize = pSrcEnd - pSrcCur;
	}

	return S_OK;
}

int CAudioMuxer::WriteFileDataImpl()
{
	AVCodecContext *c = m_pAudioStream->codec;
	AVPacket pkt = { 0 }; // data and size must be 0;
	AVFrame *frame = avcodec_alloc_frame();
	int nGotPacket;

	av_init_packet(&pkt);

	frame->nb_samples = m_nFrameSize;
	avcodec_fill_audio_frame(frame, c->channels, c->sample_fmt, m_pSamples, m_nBufSize, 1);

	avcodec_encode_audio2(c, &pkt, frame, &nGotPacket);
	if (!nGotPacket) {
		return E_FAIL;
	}

	pkt.stream_index = m_pAudioStream->index;

	/* Write the compressed frame to the media file. */
	if (av_interleaved_write_frame(m_pCtx, &pkt) != 0) {
		Log("Error while writing audio frame\n");
		return E_FAIL;
	}
	avcodec_free_frame(&frame);

	return S_OK;
}

int CAudioMuxer::WriteFileTrailer()
{
	/* Write the trailer, if any. The trailer must be written before you
	 * close the CodecContexts open when you wrote the header; otherwise
	 * av_write_trailer() may try to use memory that was freed on
	 * av_codec_close(). */
	av_write_trailer(m_pCtx);

	return S_OK;
}

