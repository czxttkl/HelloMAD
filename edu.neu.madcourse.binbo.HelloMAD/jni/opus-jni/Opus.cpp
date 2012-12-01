#include <string.h>
#include "CallbackManager.h"
#include "Opus.h"
#include "AudioEncoder.h"
#include "AudioDecoder.h"
#include "AudioMuxer.h"


CCallbackManager* g_CallbackManager = CCallbackManager::GetInstance();

COpus::EventParam::EventParam()
{
    memset(this, 0, sizeof(EventParam));
}

COpus::EventParam::EventParam(int nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData, void* pReserved)
{
    this->nEvent    = nEvent;
    this->dwParam1  = dwParam1;
    this->dwParam2  = dwParam2;
    this->pUserData = pUserData;
    this->pReserved = pReserved;
}

COpus::COpus()
{
	avcodec_register_all();

	m_pEncoder = new CAudioEncoder(this);
	m_pDecoder = new CAudioDecoder(this);
	m_pMuxer   = new CAudioMuxer(this);
}

COpus::~COpus()
{
	if (m_pEncoder) {
		delete m_pEncoder;
		m_pEncoder = NULL;
	}

	if (m_pDecoder) {
		delete m_pDecoder;
		m_pDecoder = NULL;
	}

	if (m_pMuxer) {
		delete m_pMuxer;
		m_pMuxer = NULL;
	}
}

COpus* COpus::GetInstance()
{
	static COpus s_Opus;

	return &s_Opus;
}

int COpus::CreateEncoder(int nBitrate, int nSampleRate, int nChannels, int nApp, const char* pszPath)
{
	AssertValid(m_pEncoder);
	int nResult = m_pEncoder->Create(nBitrate, nSampleRate, nChannels, nApp, pszPath);

	return nResult;
}

int COpus::CreateDecoder(const char* pszPath)
{
	AssertValid(m_pDecoder);
	int nResult = m_pDecoder->Create(pszPath);

	return nResult;
}

int COpus::DestroyEncoder()
{
	AssertValid(m_pEncoder);
	int nResult = m_pEncoder->Destroy();

	return nResult;
}

int COpus::DestroyDecoder()
{
	AssertValid(m_pDecoder);
	int nResult = m_pDecoder->Destroy();

	return nResult;
}

int COpus::Encode(BYTE* pInPCM, int nSize)
{
	AssertValid(m_pEncoder);
	int nResult = m_pEncoder->Encode(pInPCM, nSize);

	return nResult;
}

int COpus::Decode(BYTE* pOutPCM, int nSize)
{
	AssertValid(m_pDecoder);
	int nProgress = 0;
	int nResult = m_pDecoder->Decode(pOutPCM, nSize, nProgress);

	return nResult;
}

int COpus::ConvertFile(const char* pszFrom, const char* pszTo)
{
	// the input parameters are temporarily ignored
	Log("in COpus::OpusToWave\n");
	int  nResult = S_OK;
	int nProgress = 0;
	int nSampleRate = 48000;
	int nChannels = 2;
	int nBitrate = 64000;
	int nSize = 4096;
	BOOL bRun = TRUE;
	BYTE* pPCM = NULL;
	CAudioDecoder* pDecoder = new CAudioDecoder(this);

	if (!pszFrom || !pszTo) {
		nResult = E_FAIL;
		goto _error;
	}
	m_bCanceled = FALSE;

	nResult = pDecoder->Create(pszFrom);
	if (nResult != S_OK) {
		Log("Can't open the decoder to convert %s to %s\n", pszFrom, pszTo);
		goto _error;
	}
	// refresh the input parameters (temporarily)
	pDecoder->GetSampleRate(&nSampleRate);
	pDecoder->GetChannels(&nChannels);
	pDecoder->GetBitrate(&nBitrate);
	// allocate buffer for storing decoded data
	nSize = nSampleRate * nChannels * 2;
	pPCM = new BYTE[nSize];
	/*************************************************************************/
	nResult = m_pMuxer->CreateOutputContext(pszTo, nBitrate, nSampleRate, nChannels);
	if (nResult != S_OK) {
		Log("Something wrong during creating the output context for wave\n");
		goto _error;
	}
	nResult = m_pMuxer->WriteFileHeader();
	if (nResult != S_OK) {
		Log("Fail to write file header\n");
		goto _error;
	}
	/*************************************************************************/
	while (bRun && !m_bCanceled) {
		int nResult = pDecoder->Decode(pPCM, nSize, nProgress);
		if (nResult != S_OK) {
			bRun = FALSE;
		}
		ReceiveEvent(this, EVENT_UPDATE_CONVERT_PROGRESS, nProgress, 0, NULL);
		nResult = m_pMuxer->WriteFileData(pPCM, nSize);
		if (nResult != S_OK) {
			Log("Fail to write file data\n");
			goto _error;
		}
	}
	nResult = m_pMuxer->WriteFileTrailer();
	Log("out COpus::ConvertFile\n");
_error:
	m_pMuxer->DestroyOutputContext();
	if (pDecoder) {
		pDecoder->Destroy();
		delete pDecoder;
	}
	if (pPCM) {
		delete pPCM;
	}

	return nResult;
}

int COpus::CancelConverting()
{
	m_bCanceled = TRUE;
	return S_OK;
}

int COpus::SetCallback(int nType, PCallback pfnCallback, void* pUserData, void* pReserved)
{
    AssertValid(g_CallbackManager);
    g_CallbackManager->SetCallback(nType, pfnCallback, pUserData, pReserved);

    return S_OK;
}

int COpus::SetParameter(int nParam, void* pValue)
{
	return E_NOIMPL;
}

int COpus::GetParameter(int nParam, void* pValue)
{
	switch (nParam) {
	case PARAM_DECODER_SAMPLERATE:
		m_pDecoder->GetSampleRate((int*)pValue);
		break;
	case PARAM_DECODER_CHANNELS:
		m_pDecoder->GetChannels((int*)pValue);
		break;
	case PARAM_DECODER_SAMPLEFORMAT:
		m_pDecoder->GetSampleFormat((int*)pValue);
		break;
	case PARAM_DECODER_BITRATE:
		m_pDecoder->GetBitrate((int*)pValue);
		break;
	}

	return S_OK;
}

inline
int COpus::FilterEvent(void* pSender, UINT nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData)
{
    return S_OK;
}

int COpus::ReceiveEvent(void* pSender, int nEvent, DWORD dwParam1, DWORD dwParam2, void* pUserData)
{
    if (FilterEvent(pSender, nEvent, dwParam1, dwParam2, pUserData) == E_HANDLED) {
        return S_OK;
    }

    EventParam param(nEvent, dwParam1, dwParam2, pUserData, NULL);

    switch (nEvent) {
    case EVENT_ENCOUNTER_ERROR:
       	OnEncounterError(pSender, param);
        break;
    case EVENT_UPDATE_CONVERT_PROGRESS:
    	OnUpdateConvertProgress(pSender, param);
    	break;
    default:
        return E_NOIMPL;
    }

    return E_HANDLED;
}

void COpus::OnEncounterError(void* pSender, EventParam& param)
{
	CallbackData cbd = g_CallbackManager->GetCallbackData(CALLBACK_ENCOUNTER_ERROR);

	(*cbd.pfnCallback)(cbd.pUserData, (void*)param.dwParam1);
}

void COpus::OnUpdateConvertProgress(void* pSender, EventParam& param)
{
	CallbackData cbd = g_CallbackManager->GetCallbackData(CALLBACK_UPDATE_CONVERT_PROGRESS);

	(*cbd.pfnCallback)(cbd.pUserData, (void*)param.dwParam1);
}
