#include "AudioDecoder.h"

#define MAX_PACKET 1500

static UINT32 CharToInt(UCHAR ucChar[4])
{
    return ((UINT32)ucChar[0] << 24) | ((UINT32)ucChar[1] << 16)
         | ((UINT32)ucChar[2] << 8) |  (UINT32)ucChar[3];
}

CAudioDecoder::CAudioDecoder(IDependency* pDepend)
	: CDependencyObject(pDepend)
{
	m_nChannels   = 0;
	m_nSampleRate = 0;

	m_pCodec     = NULL;
	m_pCodecCtx  = NULL;
	m_pFormatCtx = NULL;

	Reset();
}

CAudioDecoder::~CAudioDecoder()
{

}

int CAudioDecoder::Reset()
{
	m_bEOS = FALSE;

	return S_OK;
}

BOOL CAudioDecoder::PrepareCodecs(AVFormatContext* pFmtCtx)
{
    AVStream** pStreams = pFmtCtx->streams;

    for (int i = 0; i < pFmtCtx->nb_streams; ++i) {
        if (!pStreams[i] || !pStreams[i]->codec) { continue; }

        AVCodec* pCodec = avcodec_find_decoder(pStreams[i]->codec->codec_id);
        if (!pCodec) { continue; }

        AssertValid(pCodec && pStreams[i]->codec);
        AVMediaType nCodecType = pStreams[i]->codec->codec_type;
        if (AVMEDIA_TYPE_AUDIO == nCodecType) {
            pStreams[i]->codec->request_channels = pStreams[i]->codec->channels > 0 ?
                FFMIN(2, pStreams[i]->codec->channels) : 2;
            pStreams[i]->codec->request_sample_fmt = AV_SAMPLE_FMT_S16;
            if (avcodec_open2(pStreams[i]->codec, pCodec, NULL) < 0)
                continue;
            pStreams[i]->codec->codec = pCodec;

            m_pCodec    = pCodec;
            m_pCodecCtx = pStreams[i]->codec;
            m_nStreamID = i;
        }
    }

    return TRUE;
}

BOOL CAudioDecoder::PrepareAudioData(AVFormatContext* pFmtCtx)
{
	// the validation of the following values is ensured in PrepareCodecs
	AVStream* pStream = m_pFormatCtx->streams[m_nStreamID];

	m_nSampleRate       = m_pCodecCtx->sample_rate;
	m_nCodecID		    = m_pCodecCtx->codec_id;
	m_nChannels			= m_pCodecCtx->channels;
	m_nFrameSize  		= m_pCodecCtx->frame_size;
	m_nBitrate	        = m_pCodecCtx->bit_rate;
	m_nSampleFormat     = m_pCodecCtx->sample_fmt;
	m_llDuration        = m_pFormatCtx->duration;
	if (pStream->time_base.den) {
		m_fTimebase = (float)pStream->time_base.num / pStream->time_base.den;
	}

    return TRUE;
}

int CAudioDecoder::Create(const char* pszPath)
{
	Log("in CAudioDecoder::Create\n")

	if (avformat_open_input(&m_pFormatCtx, pszPath, NULL, NULL) != 0) {
		//NotifyEvent(EVENT_ENCOUNTER_ERROR, E_IO, 0, NULL);
		return E_FAIL;
	}
	if (avformat_find_stream_info(m_pFormatCtx, NULL) < 0) {
		//NotifyEvent(EVENT_ENCOUNTER_ERROR, E_BADSTREAM, 0, NULL);
		return E_FAIL;
	}
	if (!PrepareCodecs(m_pFormatCtx)) { // can't find both audio/video codecs
		//NotifyEvent(EVENT_ENCOUNTER_ERROR, E_NOCODECS, 0, NULL);
		return E_FAIL;
	}
	if (!PrepareAudioData(m_pFormatCtx)) {
		//NotifyEvent(EVENT_ENCOUNTER_ERROR, E_BADSTREAM, 0, NULL);
		return E_FAIL;
	}

	m_nPCMBufBeg = m_nPCMBufEnd = 0;

	Log("out CAudioDecoder::Create\n");
	return S_OK;
}

int CAudioDecoder::Destroy()
{
	Log("in CAudioDecoder::Destroy\n");

	if (m_pCodecCtx) {
		avcodec_close(m_pCodecCtx);
		m_pCodecCtx = NULL;
	}

	if (m_pFormatCtx) {
		avformat_close_input(&m_pFormatCtx);
	}

	Reset();

	Log("out CAudioDecoder::Destroy\n");
	return 0;
}

BOOL CAudioDecoder::ReadPacket(AVPacket& rPacket)
{
    int nRet = av_read_frame(m_pFormatCtx, &rPacket);

    if (nRet == 0) {
        return TRUE;
    }

    switch (nRet) {
    case AVERROR(EPERM):
    case AVERROR_EOF:
    	m_bEOS = TRUE;
        break;
    case AVERROR(EIO):
        m_bEOS = TRUE;
        break;
    default:
        break;
    }

    return FALSE;
}

int CAudioDecoder::Decode(BYTE* pbOutPCM, int nSize, int& nProgress)
{
	BYTE* pDst = pbOutPCM;
	BYTE* pEnd = pbOutPCM + nSize;

	while (1) {
		int nLeft = m_nPCMBufEnd - m_nPCMBufBeg;
		if (nLeft >= nSize) {
			memcpy(pDst, m_pPCMBuf + m_nPCMBufBeg, nSize);
			m_nPCMBufBeg += nSize;
			break;
		} else {
			memcpy(pDst, m_pPCMBuf + m_nPCMBufBeg, nLeft);
			pDst += nLeft;
			nSize -= nLeft;
			m_nPCMBufBeg = m_nPCMBufEnd = 0; // prepair for decoding
		}

		if (!ReadPacket(m_packet)) {
			memset(pDst, 0, pEnd - pDst);
			return E_EOF;
		}
		AVPacket tmp = m_packet;
		int nResult = DecodePacket(m_packet, nProgress);
		if (nResult != S_OK) {
			memset(pDst, 0, pEnd - pDst);
			return nResult;
		}
		nProgress = UpdateProgress(tmp);
		Log("progress = %d\n", nProgress);
		av_free_packet(&tmp);
	}

	return S_OK;
}

int CAudioDecoder::DecodePacket(AVPacket& rPacket, int& nProgress)
{
	AssertValid(m_nPCMBufEnd == 0);

	while (rPacket.size > 0) {
		int nLength = 0, nGotFrame = 0, nSize = AVCODEC_MAX_AUDIO_FRAME_SIZE;

		nLength = avcodec_decode_audio3(m_pCodecCtx, (INT16*)(m_pPCMBuf + m_nPCMBufEnd), &nSize, &rPacket);
		if (nLength < 0) {
			Log("Error while decoding\n");
			exit(1);
		}

		m_nPCMBufEnd += nSize;
		rPacket.size -= nLength;
		rPacket.data += nLength;
	}

	return S_OK;
}

int CAudioDecoder::UpdateProgress(AVPacket& rPacket)
{
	LONGLONG llCur  = avio_tell(m_pFormatCtx->pb);
	LONGLONG llSize = avio_size(m_pFormatCtx->pb);
	return (float)llCur / llSize * 100;
}

int CAudioDecoder::GetSampleRate(int* pSampleRate)
{
	*pSampleRate = m_nSampleRate;
	return S_OK;
}

int CAudioDecoder::GetChannels(int* pChannels)
{
	*pChannels = m_nChannels;
	return S_OK;
}

int CAudioDecoder::GetSampleFormat(int* pSampleFormat)
{
	*pSampleFormat = m_nSampleFormat;
	return S_OK;
}

int CAudioDecoder::GetBitrate(int* pBitrate)
{
	*pBitrate = m_nBitrate;
	return S_OK;
}




